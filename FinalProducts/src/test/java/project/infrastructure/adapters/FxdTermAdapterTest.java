package project.infrastructure.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import project.domain.model.account.FixedTermAccount;
import project.domain.validations.SaveDomainValidations;
import project.domain.validations.UpdateDomainValidations;
import project.infrastructure.adapters.mongorepo.FxdTermRepo;
import project.infrastructure.clientcalls.responses.Client;
import project.infrastructure.dto.FxdTermDTO;
import project.infrastructure.exceptions.throwable.InvalidRule;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Clase de prueba para FxdTermAdapter.
 */
class FxdTermAdapterTest {

  private final FxdTermDTO testFxdTermDTO = new FxdTermDTO();
  private final Client testClient = new Client();
  private final FixedTermAccount foundFxdAccount = new FixedTermAccount();

  @Mock
  private FxdTermRepo fxdTermRepo;
  @Mock
  private SaveDomainValidations saveDomainValidations;
  @Mock
  private UpdateDomainValidations updateDomainValidations;
  @Mock
  private ReactiveMongoTemplate reactiveMongoTemplate;
  @InjectMocks
  private FxdTermAdapter fxdTermAdapter;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  /**
   * Prueba que verifica que no se puede actualizar una cuenta a plazo fijo si existen cuentas de otro tipo.
   */
  @Test
  void shouldNotUpdateFixedTermAccount() {
    when(saveDomainValidations.validateFxdTermAccount(any(Client.class)))
        .thenReturn(Mono.just(true)); // Valida si existen cuentas de otro tipo, por eso true

    StepVerifier.create(fxdTermAdapter.save(testFxdTermDTO, testClient))
        .expectError(InvalidRule.class)
        .verify();

    verify(saveDomainValidations).validateFxdTermAccount(any(Client.class));
    verify(fxdTermRepo, never()).insert(any(FixedTermAccount.class));
  }

  /**
   * Prueba que verifica que se puede guardar una cuenta a plazo fijo.
   */
  @Test
  void shouldSaveFixedTermAccount() {
    when(saveDomainValidations.validateFxdTermAccount(any(Client.class)))
        .thenReturn(Mono.just(false));
    when(fxdTermRepo.insert(any(FixedTermAccount.class)))
        .thenReturn(Mono.just(new FixedTermAccount()));

    StepVerifier.create(fxdTermAdapter.save(testFxdTermDTO, testClient))
        .expectNextCount(1)
        .verifyComplete();

    verify(saveDomainValidations).validateFxdTermAccount(any(Client.class));
    verify(fxdTermRepo).insert(any(FixedTermAccount.class));
  }

  /**
   * Prueba que verifica que se puede actualizar una cuenta a plazo fijo válida.
   */
  @Test
  void shouldUpdateValidAccount() {
    foundFxdAccount.setAccountNumber("123");
    when(updateDomainValidations.validateAmmount(any(FxdTermDTO.class)))
        .thenReturn(Mono.just(testFxdTermDTO));
    when(reactiveMongoTemplate.findAndModify(any(Query.class), any(UpdateDefinition.class), eq(FixedTermAccount.class)))
        .thenReturn(Mono.just(foundFxdAccount));

    StepVerifier.create(fxdTermAdapter.update(foundFxdAccount, testFxdTermDTO))
        .expectNext("Account updated successfully")
        .verifyComplete();

    verify(updateDomainValidations).validateAmmount(any(FxdTermDTO.class));
    verify(reactiveMongoTemplate).findAndModify(any(Query.class), any(UpdateDefinition.class), eq(FixedTermAccount.class));
  }

  /**
   * Prueba que verifica que no se puede actualizar una cuenta a plazo fijo con un monto inválido.
   */
  @Test
  void shouldNotUpdateInvalidAmmount() {
    when(updateDomainValidations.validateAmmount(any(FxdTermDTO.class)))
        .thenReturn(Mono.error(new InvalidRule("Invalid amount")));

    StepVerifier.create(fxdTermAdapter.update(foundFxdAccount, testFxdTermDTO))
        .expectError(InvalidRule.class)
        .verify();

    verify(updateDomainValidations).validateAmmount(any(FxdTermDTO.class));
    verify(reactiveMongoTemplate, never()).findAndModify(any(Query.class), any(UpdateDefinition.class), eq(FixedTermAccount.class));
  }
}