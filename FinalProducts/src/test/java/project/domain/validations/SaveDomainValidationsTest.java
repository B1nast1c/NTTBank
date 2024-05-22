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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SaveDomainValidationsTest {
  private final Client testClient = new Client();
  private final BankAccountDTO testAcc = new BankAccountDTO();
  private final CurrAccDTO currTestAcc = new CurrAccDTO();
  private final Set<String> testTitulars = new HashSet<>();

  @Mock
  private CurrAccRepo currAccRepo;

  @Mock
  private SavingsRepo savingsRepo;

  @Mock
  private FxdTermRepo fxdTermRepo;

  @InjectMocks
  private SaveDomainValidations saveDomainValidations;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    when(currAccRepo.existsByClientDocument(any())).thenReturn(Mono.just(false));
    when(savingsRepo.existsByClientDocument(any())).thenReturn(Mono.just(false));
    when(fxdTermRepo.existsByClientDocument(any())).thenReturn(Mono.just(false));
    testClient.setClientType("EMPRESARIAL");
    testAcc.setAccountType("CUENTA_CORRIENTE");
  }

  @Test
  void shouldValidateFixedPersonalHasNotAny() {
    testClient.setClientType("PERSONAL");
    testClient.setClientType("PLAZO_FIJO");
    SaveDomainValidations saveDomainValidations = new SaveDomainValidations(currAccRepo, savingsRepo, fxdTermRepo);

    StepVerifier.create(saveDomainValidations
            .validateFxdTermAccount(testClient))
        .expectNext(false)
        .verifyComplete();
  }

  @Test
  void shouldValidateFixedPersonalHasSavingsOrCurrent() {
    testClient.setClientType("PERSONAL");
    testClient.setClientType("PLAZO_FIJO");
    when(savingsRepo.existsByClientDocument(any())).thenReturn(Mono.just(true));
    SaveDomainValidations saveDomainValidations = new SaveDomainValidations(currAccRepo, savingsRepo, fxdTermRepo);

    StepVerifier.create(saveDomainValidations
            .validateFxdTermAccount(testClient))
        .expectNext(true)
        .verifyComplete();
  }

  @Test
  void shouldValidateCurrentPersonalHasNotAny() {
    testClient.setClientType("PERSONAL");
    SaveDomainValidations saveDomainValidations = new SaveDomainValidations(currAccRepo, savingsRepo, fxdTermRepo);

    StepVerifier.create(saveDomainValidations
            .validateCurrentAccount(testClient, currTestAcc))
        .expectNext(true)
        .verifyComplete();
  }

  @Test
  void shouldValidateCurrentPersonalHasAny() {
    testClient.setClientType("PERSONAL");
    when(currAccRepo.existsByClientDocument(any())).thenReturn(Mono.just(true));
    SaveDomainValidations saveDomainValidations = new SaveDomainValidations(currAccRepo, savingsRepo, fxdTermRepo);

    StepVerifier.create(saveDomainValidations
            .validateCurrentAccount(testClient, currTestAcc))
        .expectNext(false)
        .verifyComplete();
  }

  @Test
  void shouldValidateCurrentPersonalHasSaving() {
    testClient.setClientType("PERSONAL");
    when(savingsRepo.existsByClientDocument(any())).thenReturn(Mono.just(true));
    SaveDomainValidations saveDomainValidations = new SaveDomainValidations(currAccRepo, savingsRepo, fxdTermRepo);

    StepVerifier.create(saveDomainValidations
            .validateCurrentAccount(testClient, currTestAcc))
        .expectNext(false)
        .verifyComplete();
  }

  @Test
  void shouldValidateCurrentEnterpriseAndEmptyTitulars() {
    SaveDomainValidations saveDomainValidations = new SaveDomainValidations(currAccRepo, savingsRepo, fxdTermRepo);

    StepVerifier.create(saveDomainValidations
            .validateCurrentAccount(testClient, currTestAcc))
        .expectNext(true)
        .verifyComplete();
  }

  @Test
  void shouldValidateCurrentEnterpriseAndHasTitulars() {
    SaveDomainValidations saveDomainValidations = new SaveDomainValidations(currAccRepo, savingsRepo, fxdTermRepo);
    testTitulars.add("testTitular");
    currTestAcc.setAccountTitulars(testTitulars);

    StepVerifier.create(saveDomainValidations
            .validateCurrentAccount(testClient, currTestAcc))
        .expectNext(true)
        .verifyComplete();
  }

  @Test
  void shouldValidateSavingsEmpresarial() {
    testAcc.setAccountType("AHORRO");
    SaveDomainValidations saveDomainValidations = new SaveDomainValidations(currAccRepo, savingsRepo, fxdTermRepo);

    StepVerifier.create(saveDomainValidations.validateSavingsAccount(testClient))
        .expectNext(true)
        .verifyComplete();
  }

  @Test
  void shouldValidateSavingsPersonal() {
    testClient.setClientType("PERSONAL");
    testAcc.setAccountType("AHORRO");
    SaveDomainValidations saveDomainValidations = new SaveDomainValidations(currAccRepo, savingsRepo, fxdTermRepo);

    StepVerifier.create(saveDomainValidations.validateSavingsAccount(testClient))
        .expectNext(false)
        .verifyComplete();
  }

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