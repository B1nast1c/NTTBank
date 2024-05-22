package project.application.service.domainService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import project.domain.ports.BAccountPort;
import project.domain.validations.SaveDomainValidations;
import project.domain.validations.UpdateDomainValidations;
import project.infrastructure.adapters.CurrAccAdapter;
import project.infrastructure.adapters.GenericAccAdapter;
import project.infrastructure.clientcalls.WebClientSrv;
import project.infrastructure.clientcalls.responses.Client;
import project.infrastructure.dto.BankAccountDTO;
import project.infrastructure.dto.CurrAccDTO;
import project.infrastructure.dto.LegalSignerDTO;
import project.infrastructure.factory.BARepoFactory;
import project.infrastructure.responses.CustomResponse;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;

class DomainBAccountServiceTest {
  @Mock
  BARepoFactory repositoryFactory;
  @Mock
  WebClientSrv clientService;
  @Mock
  GenericAccAdapter genericRepository;
  @Mock
  BAccountPort repository;
  @InjectMocks
  DomainBAccountService domainBAccountService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testCreateBankAccount() {
    when(repositoryFactory.getAdapter(anyString())).thenReturn(new CurrAccAdapter(null, new SaveDomainValidations(null, null, null), new WebClientSrv(null), new UpdateDomainValidations(), new ReactiveMongoTemplate(null, null, null)));
    when(clientService.getClientByiD(anyString())).thenReturn(null);
    when(repository.save(any(Object.class), any(Client.class))).thenReturn(null);

    Mono<CustomResponse<Object>> result = domainBAccountService.createBankAccount("account");
    Assertions.assertNull(result);
  }

  @Test
  void testGetBankAccount() {
    when(genericRepository.findByAccountNumber(anyString())).thenReturn(null);

    Mono<CustomResponse<Object>> result = domainBAccountService.getBankAccount("accountNumber");
    Assertions.assertNull(result);
  }

  @Test
  void testUpdateBankAccount() {
    when(repositoryFactory.getAdapter(anyString())).thenReturn(new CurrAccAdapter(null, new SaveDomainValidations(null, null, null), new WebClientSrv(null), new UpdateDomainValidations(), new ReactiveMongoTemplate(null, null, null)));
    when(genericRepository.findByAccountNumber(anyString())).thenReturn(null);
    when(repository.update(any(Object.class), any(Object.class))).thenReturn(null);

    Mono<CustomResponse<Object>> result = domainBAccountService.updateBankAccount("accountNumber", new BankAccountDTO("id", "clientDocument", "accountNumber", 0d, 0, "accountType"));
    Assertions.assertNull(result);
  }

  @Test
  void testDeleteBankAccount() {
    Mono<CustomResponse<Object>> result = domainBAccountService.deleteBankAccount("accountNumber");
    Assertions.assertNull(result);
  }

  @Test
  void testGetBankAccounts() {
    when(genericRepository.findAll()).thenReturn(null);

    Mono<CustomResponse<List<Object>>> result = domainBAccountService.getBankAccounts();
    Assertions.assertNull(result);
  }

  @Test
  void testGetAllBankAccountsByClientId() {
    when(genericRepository.findByClientId(anyString())).thenReturn(null);

    Mono<CustomResponse<Object>> result = domainBAccountService.getAllBankAccountsByClientId("clientId");
    Assertions.assertNull(result);
  }

  @Test
  void testAddTitularsToAccount() {
    Mono<CustomResponse<CurrAccDTO>> result = domainBAccountService.addTitularsToAccount("accountNumber", List.of("titulars"));
    Assertions.assertNull(result);
  }

  @Test
  void testAddLegalSignersToAccount() {
    Mono<CustomResponse<CurrAccDTO>> result = domainBAccountService.addLegalSignersToAccount("accountNumber", List.of(new LegalSignerDTO("signerNumber", "name", "lastName")));
    Assertions.assertNull(result);
  }

  @Test
  void testRemoveTitularfromAccount() {
    Mono<CustomResponse<CurrAccDTO>> result = domainBAccountService.removeTitularfromAccount("accountNumber", "titularId");
    Assertions.assertNull(result);
  }

  @Test
  void testRemoveLegalSignerfromAccount() {
    Mono<CustomResponse<CurrAccDTO>> result = domainBAccountService.removeLegalSignerfromAccount("accountNumber", "signerId");
    Assertions.assertNull(result);
  }

  @Test
  void testGetAccountBalance() {
    when(genericRepository.findByAccountNumber(anyString())).thenReturn(null);

    Mono<CustomResponse<Object>> result = domainBAccountService.getAccountBalance("accountNumber");
    Assertions.assertNull(result);
  }
}