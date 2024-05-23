package project.application.service.domainService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import project.domain.ports.BAccountPort;
import project.infrastructure.adapters.GenericAccAdapter;
import project.infrastructure.adapters.SavingsAccAdapter;
import project.infrastructure.clientcalls.WebClientSrv;
import project.infrastructure.clientcalls.responses.Client;
import project.infrastructure.clientcalls.responses.ClientResponse;
import project.infrastructure.dto.BankAccountDTO;
import project.infrastructure.exceptions.CustomError;
import project.infrastructure.exceptions.throwable.InvalidRule;
import project.infrastructure.exceptions.throwable.NotFound;
import project.infrastructure.exceptions.throwable.WrongAccountType;
import project.infrastructure.factory.BARepoFactory;
import project.infrastructure.mapper.GenericMapper;
import project.infrastructure.responses.CustomResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DomainBAccountServiceTest {
  private final Object testAccount = new Object();
  private final BankAccountDTO testAccountDTO = new BankAccountDTO();
  BAccountPort repository = mock(SavingsAccAdapter.class);
  ClientResponse clientResponse = new ClientResponse(true, new Client(
      "ClientId",
      "PERSONAL",
      "Valeria",
      "Address",
      "Mail",
      "Phone",
      "Number",
      true
  ));
  BankAccountDTO objectAccount = new BankAccountDTO();

  @Mock
  SavingsAccAdapter savingsAdapter;
  @Mock
  BARepoFactory repositoryFactory;
  @Mock
  WebClientSrv clientService;
  @Mock
  GenericAccAdapter genericAdapter;
  @InjectMocks
  DomainBAccountService domainBAccountService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    testAccountDTO.setAccountType("AHORRO");
    testAccountDTO.setClientDocument("ClientDocument");
    objectAccount = GenericMapper.mapToSpecificClass(testAccountDTO, BankAccountDTO.class);

    // Compotamientos para el update
    when(genericAdapter.findByAccountNumber(anyString())).thenReturn(Mono.just(testAccount));
    when(repositoryFactory.getAdapter(anyString())).thenReturn(savingsAdapter);
    when(savingsAdapter.update(any(Object.class), any(Object.class))).thenReturn(Mono.just(testAccountDTO));
    when(repository.update(any(Object.class), any(Object.class))).thenReturn(Mono.just(testAccount));
    // Comportamientos para el create / save
    when(clientService.getClientByiD(anyString())).thenReturn(Mono.just(clientResponse));
    when(savingsAdapter.save(any(Object.class), any(Client.class))).thenReturn(Mono.just(testAccountDTO));
    when(repository.save(any(Object.class), any(Client.class))).thenReturn(Mono.just(testAccount));
    when(clientService.getClientByiD(anyString())).thenReturn(Mono.just(clientResponse));
  }

  @Test
  void shouldGetBankAccountIfExists() {
    CustomResponse<Object> expectedResponse = new CustomResponse<>(true, testAccount);
    Mono<CustomResponse<Object>> result = domainBAccountService.getBankAccount("accountNumber");

    StepVerifier.create(result)
        .expectNext(expectedResponse)
        .verifyComplete();
  }

  @Test
  void shouldNotGetBankAccountNotExists() {
    String errorMessage = "Account does not exist";
    when(genericAdapter.findByAccountNumber(anyString()))
        .thenReturn(Mono.error(new NotFound(errorMessage)));

    Mono<CustomResponse<Object>> result = domainBAccountService.getBankAccount("accountNumber");

    StepVerifier.create(result)
        .assertNext(response -> {
          assertFalse(response.isSuccess());
          CustomError mappedError = GenericMapper.mapToSpecificClass(response.getData(), CustomError.class);
          assertEquals(errorMessage, mappedError.getErrorMessage());
          assertEquals(CustomError.ErrorType.GET_ERROR, mappedError.getErrorType());
        })
        .verifyComplete();
  }

  @Test
  void shouldGetBankAccountBalanceIfExists() {
    BankAccountDTO mappedAccount = GenericMapper.mapToSpecificClass(testAccount, BankAccountDTO.class);
    CustomResponse<Object> expectedResponse = new CustomResponse<>(true, mappedAccount.getBalance());
    Mono<CustomResponse<Object>> result = domainBAccountService.getAccountBalance("accountNumber");

    StepVerifier.create(result)
        .expectNext(expectedResponse)
        .verifyComplete();
  }

  @Test
  void shouldNotGetBankAccountBalanceIfNotExists() {
    String errorMessage = "Account does not exist";
    when(genericAdapter.findByAccountNumber(anyString()))
        .thenReturn(Mono.error(new NotFound(errorMessage)));

    Mono<CustomResponse<Object>> result = domainBAccountService.getAccountBalance("accountNumber");

    StepVerifier.create(result)
        .assertNext(response -> {
          assertFalse(response.isSuccess());
          CustomError mappedError = GenericMapper.mapToSpecificClass(response.getData(), CustomError.class);
          assertEquals(errorMessage, mappedError.getErrorMessage());
          assertEquals(CustomError.ErrorType.GET_ERROR, mappedError.getErrorType());
        })
        .verifyComplete();
  }

  @Test
  void shouldGetBankAccounts() {
    List<Object> accounts = List.of(testAccount);
    CustomResponse<Flux<Object>> expectedResponse = new CustomResponse<>(true, Flux.fromIterable(accounts));
    when(genericAdapter.findAll()).thenReturn(expectedResponse.getData());

    Mono<CustomResponse<List<Object>>> result = domainBAccountService.getBankAccounts();
    StepVerifier.create(result)
        .expectNextMatches(response -> response.isSuccess() && response.getData() != Flux.empty())
        .verifyComplete();
  }

  @Test
  void shouldGetBankAccountsByClientIfExist() {
    List<Object> accounts = List.of(testAccount);
    CustomResponse<Flux<Object>> expectedResponse = new CustomResponse<>(true, Flux.fromIterable(accounts));
    when(genericAdapter.findByClientId(anyString()))
        .thenReturn(expectedResponse.getData());

    Mono<CustomResponse<Object>> result = domainBAccountService.getAllBankAccountsByClientId("clientID");
    StepVerifier.create(result)
        .expectNextMatches(response -> response.isSuccess() && response.getData() != Flux.empty())
        .verifyComplete();
  }

  @Test
  void testShouldUpdateBankAccount() {
    String accountNumber = "updatedNumber";
    BankAccountDTO testUpdated = GenericMapper.mapToSpecificClass(objectAccount, BankAccountDTO.class);
    CustomResponse<BankAccountDTO> expectedResponse = new CustomResponse<>(true, testUpdated);
    when(genericAdapter.findByAccountNumber(anyString())).thenReturn(Mono.just(objectAccount));

    Mono<CustomResponse<Object>> result = domainBAccountService.updateBankAccount(accountNumber, testUpdated);

    StepVerifier.create(result)
        .assertNext(response -> {
          assertTrue(response.isSuccess());
          BankAccountDTO mappedRes = GenericMapper.mapToSpecificClass(response.getData(), BankAccountDTO.class);
          assertEquals(expectedResponse.getData().getBalance(), mappedRes.getBalance());
          assertEquals(expectedResponse.getData().getTransactions(), mappedRes.getTransactions());
        })
        .verifyComplete();
  }

  @Test
  void testShouldNopUpdateBankAccountNegativeTransaction() {
    String accountNumber = "updatedNumber";
    String errorMessage = "Transactions ammount must be positive";
    BankAccountDTO testUpdated = GenericMapper.mapToSpecificClass(objectAccount, BankAccountDTO.class);
    testUpdated.setTransactions(-10); // Seteo de transacciones negativas
    when(genericAdapter.findByAccountNumber(anyString()))
        .thenReturn(Mono.just(objectAccount));
    when(savingsAdapter.update(any(Object.class), any(Object.class)))
        .thenReturn(Mono.error(new InvalidRule("Transactions ammount must be positive")));

    Mono<CustomResponse<Object>> result = domainBAccountService.updateBankAccount(accountNumber, testUpdated);

    StepVerifier.create(result)
        .assertNext(response -> {
          assertFalse(response.isSuccess());
          CustomError mappedRes = GenericMapper.mapToSpecificClass(response.getData(), CustomError.class);
          assertEquals(errorMessage, mappedRes.getErrorMessage());
        })
        .verifyComplete();
  }

  @Test
  void testShouldCreateAccountClientExists() {
    BankAccountDTO testCreated = GenericMapper.mapToSpecificClass(objectAccount, BankAccountDTO.class);
    CustomResponse<BankAccountDTO> expectedResponse = new CustomResponse<>(true, testCreated);
    when(genericAdapter.findByAccountNumber(anyString())).thenReturn(Mono.just(objectAccount));

    Mono<CustomResponse<Object>> result = domainBAccountService.createBankAccount(testCreated);

    StepVerifier.create(result)
        .assertNext(response -> {
          assertTrue(response.isSuccess());
          BankAccountDTO mappedRes = GenericMapper.mapToSpecificClass(response.getData(), BankAccountDTO.class);
          assertEquals(expectedResponse.getData().getBalance(), mappedRes.getBalance());
          assertEquals(expectedResponse.getData().getTransactions(), mappedRes.getTransactions());
        })
        .verifyComplete();
  }

  @Test
  void testShouldNotCreateAccountClientNotExists() {
    CustomError error = new CustomError("Client retrieval failed, please try again", CustomError.ErrorType.POST_ERROR);
    CustomResponse<CustomError> expectedResponse = new CustomResponse<>(false, error);
    BankAccountDTO testCreated = GenericMapper.mapToSpecificClass(objectAccount, BankAccountDTO.class);
    clientResponse.setSuccess(false); // Cliente que crea la cuenta no se encuentra en la BD

    when(clientService.getClientByiD(anyString())).thenReturn(Mono.just(clientResponse));
    when(savingsAdapter.save(any(Object.class), any(Client.class))).thenReturn(Mono.just(testAccountDTO));
    when(repository.save(any(Object.class), any(Client.class))).thenReturn(Mono.just(testAccount));

    Mono<CustomResponse<Object>> result = domainBAccountService.createBankAccount(testCreated);

    StepVerifier.create(result)
        .assertNext(response -> {
          assertFalse(response.isSuccess());
          CustomError mappedRes = GenericMapper.mapToSpecificClass(response.getData(), CustomError.class);
          assertEquals(expectedResponse.getData().getErrorMessage(), mappedRes.getErrorMessage());
          assertEquals(expectedResponse.getData().getErrorType(), mappedRes.getErrorType());
        })
        .verifyComplete();
  }

  @Test
  void testShouldNotCreateAccountWrongType() {
    BankAccountDTO testCreated = GenericMapper.mapToSpecificClass(objectAccount, BankAccountDTO.class);
    testCreated.setAccountType("INCORRECT");
    CustomError error = new CustomError("Invalid account type", CustomError.ErrorType.POST_ERROR);
    CustomResponse<CustomError> expectedResponse = new CustomResponse<>(false, error);

    when(genericAdapter.findByAccountNumber(anyString())).thenReturn(Mono.just(objectAccount));
    when(clientService.getClientByiD(anyString())).thenReturn(Mono.just(clientResponse));
    when(savingsAdapter.save(any(Object.class), any(Client.class))).thenReturn((Mono.error(
        new WrongAccountType("Invalid account type")
    )));

    Mono<CustomResponse<Object>> result = domainBAccountService.createBankAccount(testCreated);

    StepVerifier.create(result)
        .assertNext(response -> {
          assertFalse(response.isSuccess());
          CustomError mappedRes = GenericMapper.mapToSpecificClass(response.getData(), CustomError.class);
          assertEquals(expectedResponse.getData().getErrorMessage(), mappedRes.getErrorMessage());
          assertEquals(expectedResponse.getData().getErrorType(), mappedRes.getErrorType());
        })
        .verifyComplete();
  }
}