package project.infrastructure.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import project.domain.model.account.CurrentAccount;
import project.domain.validations.SaveDomainValidations;
import project.domain.validations.UpdateDomainValidations;
import project.infrastructure.adapters.mongorepo.CurrAccRepo;
import project.infrastructure.clientcalls.WebClientSrv;
import project.infrastructure.clientcalls.responses.Client;
import project.infrastructure.clientcalls.responses.ClientResponse;
import project.infrastructure.dto.CurrAccDTO;
import project.infrastructure.dto.LegalSignerDTO;
import project.infrastructure.exceptions.throwable.EmptyAttributes;
import project.infrastructure.exceptions.throwable.InvalidRule;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Clase de prueba para CurrAccAdapter.
 */
@ExtendWith(MockitoExtension.class)
class CurrAccAdapterTest {
  private final CurrAccDTO testCurrAccDTO = new CurrAccDTO();
  private final Client testClient = new Client();
  private final CurrentAccount foundCurrentAccount = new CurrentAccount();
  @Mock
  private CurrAccRepo currentRepo;
  @Mock
  private SaveDomainValidations domainValidations;
  @Mock
  private WebClientSrv clientService;
  @Mock
  private UpdateDomainValidations updateDomainValidations;
  @Mock
  private ReactiveMongoTemplate reactiveMongoTemplate;
  @InjectMocks
  private CurrAccAdapter currAccAdapter;

  @BeforeEach
  void setUp() {
    testCurrAccDTO.setAccountTitulars(new HashSet<>(Set.of("titular1", "titular2")));
    testClient.setClientType("EMPRESARIAL");
  }

  /**
   * Prueba que verifica que se puede guardar una cuenta corriente empresarial.
   */
  @Test
  void shouldSaveCurrentEnterpriseAccount() {
    when(domainValidations.validateCurrentAccount(any(Client.class), any(CurrAccDTO.class)))
        .thenReturn(Mono.just(true));
    when(clientService.getClientByiD(anyString()))
        .thenReturn(Mono.just(new ClientResponse(true, testClient)));
    when(currentRepo.insert(any(CurrentAccount.class)))
        .thenReturn(Mono.just(new CurrentAccount()));

    StepVerifier.create(currAccAdapter.save(testCurrAccDTO, testClient))
        .expectNextCount(1)
        .verifyComplete();

    verify(domainValidations).validateCurrentAccount(any(Client.class), any(CurrAccDTO.class));
    verify(clientService, times(2)).getClientByiD(anyString());
    verify(currentRepo).insert(any(CurrentAccount.class));
  }

  /**
   * Prueba que verifica que se puede guardar una cuenta corriente empresarial con firmantes.
   */
  @Test
  void shouldSaveCurrentEnterpriseAccountWithSigners() {
    testCurrAccDTO.setLegalSigners(List.of(new LegalSignerDTO[]{new LegalSignerDTO(), new LegalSignerDTO()}));
    when(domainValidations.validateCurrentAccount(any(Client.class), any(CurrAccDTO.class)))
        .thenReturn(Mono.just(true));
    when(clientService.getClientByiD(anyString()))
        .thenReturn(Mono.just(new ClientResponse(true, testClient)));
    when(currentRepo.insert(any(CurrentAccount.class)))
        .thenReturn(Mono.just(new CurrentAccount()));

    StepVerifier.create(currAccAdapter.save(testCurrAccDTO, testClient))
        .expectNextCount(1)
        .verifyComplete();

    verify(domainValidations).validateCurrentAccount(any(Client.class), any(CurrAccDTO.class));
    verify(clientService, times(2)).getClientByiD(anyString());
    verify(currentRepo).insert(any(CurrentAccount.class));
  }

  /**
   * Prueba que verifica que no se puede guardar una cuenta corriente sin titulares.
   */
  @Test
  void shouldNotSaveCurrentAccountEmptyTitulars() {
    testCurrAccDTO.setAccountTitulars(Collections.emptySet());
    when(domainValidations.validateCurrentAccount(any(Client.class), any(CurrAccDTO.class)))
        .thenReturn(Mono.just(true));

    StepVerifier.create(currAccAdapter.save(testCurrAccDTO, testClient))
        .verifyError(EmptyAttributes.class);

    verify(domainValidations).validateCurrentAccount(any(Client.class), any(CurrAccDTO.class));
    verify(clientService, times(0)).getClientByiD(anyString());
    verify(currentRepo, times(0)).insert(any(CurrentAccount.class));
  }

  /**
   * Prueba que verifica que se puede guardar una cuenta corriente personal.
   */
  @Test
  void shouldSaveCurrentPersonalAccount() {
    testClient.setClientType("PERSONAL");
    when(domainValidations.validateCurrentAccount(any(Client.class), any(CurrAccDTO.class)))
        .thenReturn(Mono.just(true));
    when(clientService.getClientByiD(anyString()))
        .thenReturn(Mono.just(new ClientResponse(true, testClient)));
    when(currentRepo.insert(any(CurrentAccount.class)))
        .thenReturn(Mono.just(new CurrentAccount()));

    StepVerifier.create(currAccAdapter.save(testCurrAccDTO, testClient))
        .expectNextCount(1)
        .verifyComplete();

    verify(domainValidations).validateCurrentAccount(any(Client.class), any(CurrAccDTO.class));
    verify(clientService, times(2)).getClientByiD(anyString());
    verify(currentRepo, times(1)).insert(any(CurrentAccount.class));
  }

  /**
   * Prueba que verifica que se puede guardar una cuenta corriente personal sin titulares.
   */
  @Test
  void shouldSaveCurrentPersonalAccountNoTitulars() {
    testClient.setClientType("PERSONAL");
    testCurrAccDTO.setAccountTitulars(new HashSet<>());
    when(domainValidations.validateCurrentAccount(any(Client.class), any(CurrAccDTO.class)))
        .thenReturn(Mono.just(true));
    when(currentRepo.insert(any(CurrentAccount.class)))
        .thenReturn(Mono.just(new CurrentAccount()));

    StepVerifier.create(currAccAdapter.save(testCurrAccDTO, testClient))
        .expectNextCount(1)
        .verifyComplete();

    verify(domainValidations).validateCurrentAccount(any(Client.class), any(CurrAccDTO.class));
    verify(currentRepo, times(1)).insert(any(CurrentAccount.class));
  }

  /**
   * Prueba que verifica que no se puede guardar una cuenta corriente si las validaciones fallan.
   */
  @Test
  void shouldNotSaveCurrentAccount() {
    when(domainValidations.validateCurrentAccount(any(Client.class), any(CurrAccDTO.class)))
        .thenReturn(Mono.just(false));

    StepVerifier.create(currAccAdapter.save(testCurrAccDTO, testClient))
        .expectError(InvalidRule.class)
        .verify();

    verify(domainValidations).validateCurrentAccount(any(Client.class), any(CurrAccDTO.class));
    verify(clientService, never()).getClientByiD(anyString());
    verify(currentRepo, never()).insert(any(CurrentAccount.class));
  }

  /**
   * Prueba que verifica que se puede actualizar una cuenta corriente existente.
   */
  @Test
  void shouldUpdateCurrentAccount() {
    foundCurrentAccount.setAccountNumber("123");
    when(updateDomainValidations.validateAmmount(any(CurrAccDTO.class)))
        .thenReturn(Mono.just(testCurrAccDTO));
    when(reactiveMongoTemplate.findAndModify(any(Query.class), any(Update.class), eq(CurrentAccount.class)))
        .thenReturn(Mono.just(foundCurrentAccount));

    StepVerifier.create(currAccAdapter.update(foundCurrentAccount, testCurrAccDTO))
        .expectNext("Account updated successfully")
        .verifyComplete();

    verify(updateDomainValidations).validateAmmount(any(CurrAccDTO.class));
    verify(reactiveMongoTemplate).findAndModify(any(Query.class), any(Update.class), eq(CurrentAccount.class));
  }

  /**
   * Prueba que verifica que no se puede actualizar una cuenta corriente con un monto inválido.
   */
  @Test
  void shouldNotUpdateByInvalidAmmount() {
    when(updateDomainValidations.validateAmmount(any(CurrAccDTO.class)))
        .thenReturn(Mono.error(new InvalidRule("Invalid amount")));

    StepVerifier.create(currAccAdapter.update(foundCurrentAccount, testCurrAccDTO))
        .expectError(InvalidRule.class)
        .verify();

    verify(updateDomainValidations).validateAmmount(any(CurrAccDTO.class));
    verify(reactiveMongoTemplate, never()).findAndModify(any(Query.class), any(Update.class), eq(CurrentAccount.class));
  }
}