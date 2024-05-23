package project.domain.validations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import project.infrastructure.adapters.mongorepo.CurrAccRepo;
import project.infrastructure.adapters.mongorepo.FxdTermRepo;
import project.infrastructure.adapters.mongorepo.SavingsRepo;
import project.infrastructure.clientcalls.responses.Client;
import project.infrastructure.dto.BankAccountDTO;
import project.infrastructure.dto.CurrAccDTO;
import project.infrastructure.dto.LegalSignerDTO;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Clase de prueba para las validaciones de dominio al guardar cuentas bancarias.
 */
class SaveDomainValidationsTest {

  private final Client testClient = new Client();
  private final BankAccountDTO testAcc = new BankAccountDTO();
  private final CurrAccDTO currTestAcc = new CurrAccDTO();
  private final Set<String> testTitulars = new HashSet<>();
  private final List<LegalSignerDTO> legalSigners = new ArrayList<>();

  @Mock
  private CurrAccRepo currAccRepo;

  @Mock
  private SavingsRepo savingsRepo;

  @Mock
  private FxdTermRepo fxdTermRepo;

  @InjectMocks
  private SaveDomainValidations saveDomainValidations;

  /**
   * Configuración inicial de los mocks y datos de prueba.
   */
  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    testClient.setClientType("EMPRESARIAL");
    testAcc.setAccountType("CUENTA_CORRIENTE");
    when(currAccRepo.existsByClientDocument(any())).thenReturn(Mono.just(false));
    when(savingsRepo.existsByClientDocument(any())).thenReturn(Mono.just(false));
    when(fxdTermRepo.existsByClientDocument(any())).thenReturn(Mono.just(false));
    legalSigners.add(new LegalSignerDTO("testSigner", "testName", "testLastName"));
    legalSigners.add(new LegalSignerDTO("testSigner", "testName", "testLastName"));
  }

  /**
   * Prueba para validar que un cliente personal no tenga ninguna cuenta a plazo fijo.
   */
  @Test
  void shouldValidateFixedPersonalHasNotAny() {
    testClient.setClientType("PERSONAL");
    testClient.setClientType("PLAZO_FIJO");
    SaveDomainValidations saveDomainValidations = new SaveDomainValidations(currAccRepo, savingsRepo, fxdTermRepo);

    StepVerifier.create(saveDomainValidations.validateFxdTermAccount(testClient))
        .expectNext(false)
        .verifyComplete();
  }

  /**
   * Prueba para validar que un cliente personal tenga cuentas de ahorro o corriente antes de abrir una cuenta a plazo fijo.
   */
  @Test
  void shouldValidateFixedPersonalHasSavingsOrCurrent() {
    testClient.setClientType("PERSONAL");
    testClient.setClientType("PLAZO_FIJO");
    when(savingsRepo.existsByClientDocument(any())).thenReturn(Mono.just(true));
    SaveDomainValidations saveDomainValidations = new SaveDomainValidations(currAccRepo, savingsRepo, fxdTermRepo);

    StepVerifier.create(saveDomainValidations.validateFxdTermAccount(testClient))
        .expectNext(true)
        .verifyComplete();
  }

  /**
   * Prueba para validar que un cliente personal no tenga ninguna cuenta corriente.
   */
  @Test
  void shouldValidateCurrentPersonalHasNotAny() {
    testClient.setClientType("PERSONAL");
    SaveDomainValidations saveDomainValidations = new SaveDomainValidations(currAccRepo, savingsRepo, fxdTermRepo);

    StepVerifier.create(saveDomainValidations.validateCurrentAccount(testClient, currTestAcc))
        .expectNext(true)
        .verifyComplete();
  }

  /**
   * Prueba para validar que un cliente personal ya tenga una cuenta corriente.
   */
  @Test
  void shouldValidateCurrentPersonalHasAny() {
    testClient.setClientType("PERSONAL");
    when(currAccRepo.existsByClientDocument(any())).thenReturn(Mono.just(true));
    SaveDomainValidations saveDomainValidations = new SaveDomainValidations(currAccRepo, savingsRepo, fxdTermRepo);

    StepVerifier.create(saveDomainValidations.validateCurrentAccount(testClient, currTestAcc))
        .expectNext(false)
        .verifyComplete();
  }

  /**
   * Prueba para validar que un cliente personal ya tenga una cuenta de ahorro.
   */
  @Test
  void shouldValidateCurrentPersonalHasSaving() {
    testClient.setClientType("PERSONAL");
    when(savingsRepo.existsByClientDocument(any())).thenReturn(Mono.just(true));
    SaveDomainValidations saveDomainValidations = new SaveDomainValidations(currAccRepo, savingsRepo, fxdTermRepo);

    StepVerifier.create(saveDomainValidations.validateCurrentAccount(testClient, currTestAcc))
        .expectNext(false)
        .verifyComplete();
  }

  /**
   * Prueba para validar que un cliente empresarial con titulares vacíos pueda abrir una cuenta corriente.
   */
  @Test
  void shouldValidateCurrentEnterpriseAndEmptyTitulars() {
    SaveDomainValidations saveDomainValidations = new SaveDomainValidations(currAccRepo, savingsRepo, fxdTermRepo);

    StepVerifier.create(saveDomainValidations.validateCurrentAccount(testClient, currTestAcc))
        .expectNext(true)
        .verifyComplete();
  }

  /**
   * Prueba para validar que un cliente empresarial con titulares y firmantes legales pueda abrir una cuenta corriente.
   */
  @Test
  void shouldValidateCurrentEnterpriseAndHasTitulars() {
    SaveDomainValidations saveDomainValidations = new SaveDomainValidations(currAccRepo, savingsRepo, fxdTermRepo);
    testTitulars.add("testTitular");
    currTestAcc.setAccountTitulars(testTitulars);
    currTestAcc.setLegalSigners(legalSigners);

    StepVerifier.create(saveDomainValidations.validateCurrentAccount(testClient, currTestAcc))
        .expectNext(true)
        .verifyComplete();
  }

  /**
   * Prueba para validar una cuenta de ahorro empresarial.
   */
  @Test
  void shouldValidateSavingsEmpresarial() {
    testAcc.setAccountType("AHORRO");
    SaveDomainValidations saveDomainValidations = new SaveDomainValidations(currAccRepo, savingsRepo, fxdTermRepo);

    StepVerifier.create(saveDomainValidations.validateSavingsAccount(testClient))
        .expectNext(true)
        .verifyComplete();
  }

  /**
   * Prueba para validar que un cliente personal no pueda abrir una cuenta de ahorro si no tiene una cuenta corriente.
   */
  @Test
  void shouldValidateSavingsPersonal() {
    testClient.setClientType("PERSONAL");
    testAcc.setAccountType("AHORRO");
    SaveDomainValidations saveDomainValidations = new SaveDomainValidations(currAccRepo, savingsRepo, fxdTermRepo);

    StepVerifier.create(saveDomainValidations.validateSavingsAccount(testClient))
        .expectNext(false)
        .verifyComplete();
  }

  /**
   * Prueba para validar que un cliente personal pueda abrir una cuenta de ahorro si ya tiene una cuenta de ahorro.
   */
  @Test
  void shouldValidateSavingsPersonalHasSaving() {
    testClient.setClientType("PERSONAL");
    testAcc.setAccountType("AHORRO");
    when(savingsRepo.existsByClientDocument(any())).thenReturn(Mono.just(true));
    SaveDomainValidations saveDomainValidations = new SaveDomainValidations(currAccRepo, savingsRepo, fxdTermRepo);

    StepVerifier.create(saveDomainValidations.validateSavingsAccount(testClient))
        .expectNext(true)
        .verifyComplete();
  }

  /**
   * Prueba para validar que un cliente personal pueda abrir una cuenta de ahorro si ya tiene una cuenta corriente.
   */
  @Test
  void shouldValidateSavingsPersonalHasCurrent() {
    testClient.setClientType("PERSONAL");
    testAcc.setAccountType("AHORRO");
    when(currAccRepo.existsByClientDocument(any())).thenReturn(Mono.just(true));
    SaveDomainValidations saveDomainValidations = new SaveDomainValidations(currAccRepo, savingsRepo, fxdTermRepo);

    StepVerifier.create(saveDomainValidations.validateSavingsAccount(testClient))
        .expectNext(true)
        .verifyComplete();
  }

  /**
   * Prueba para validar que un cliente personal pueda abrir una cuenta de ahorro si ya tiene una cuenta a plazo fijo.
   */
  @Test
  void shouldValidateSavingsPersonalHasFxdTerm() {
    testClient.setClientType("PERSONAL");
    testAcc.setAccountType("AHORRO");
    when(fxdTermRepo.existsByClientDocument(any())).thenReturn(Mono.just(true));
    SaveDomainValidations saveDomainValidations = new SaveDomainValidations(currAccRepo, savingsRepo, fxdTermRepo);

    StepVerifier.create(saveDomainValidations.validateSavingsAccount(testClient))
        .expectNext(true)
        .verifyComplete();
  }
}