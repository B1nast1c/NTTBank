package project.infrastructure.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import project.domain.model.account.SavingsAccount;
import project.domain.validations.SaveDomainValidations;
import project.domain.validations.UpdateDomainValidations;
import project.infrastructure.adapters.mongorepo.SavingsRepo;
import project.infrastructure.clientcalls.responses.Client;
import project.infrastructure.dto.SavingsDTO;
import project.infrastructure.exceptions.throwable.InvalidRule;
import project.infrastructure.mapper.GenericMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SavingsAccAdapterTest {

  private final SavingsDTO testSavingsDTO = new SavingsDTO();
  private final SavingsAccount testSavingsAccount = new SavingsAccount();
  private final Client testClient = new Client();
  @Mock
  private SavingsRepo savingsRepo;
  @Mock
  private SaveDomainValidations saveDomainValidations;
  @Mock
  private UpdateDomainValidations updateDomainValidations;
  @Mock
  private ReactiveMongoTemplate reactiveMongoTemplate;
  @InjectMocks
  private SavingsAccAdapter savingsAccAdapter;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    when(saveDomainValidations.validateSavingsAccount(any(Client.class)))
        .thenReturn(Mono.just(false));
    when(savingsRepo.insert(any(SavingsAccount.class)))
        .thenReturn(Mono.just(testSavingsAccount));
    when(updateDomainValidations.validateSavingsAccount(any(SavingsDTO.class), any(SavingsAccount.class)))
        .thenReturn(Mono.just(true));
    when(updateDomainValidations.validateAmmount(any()))
        .thenReturn(Mono.just(testSavingsDTO));
    when(reactiveMongoTemplate
        .findAndModify(any(Query.class), any(UpdateDefinition.class), any(Class.class)))
        .thenReturn(Mono.just(testSavingsAccount));
  }

  @Test
  void shouldSaveSavingsAccount() {
    StepVerifier.create(savingsAccAdapter.save(testSavingsDTO, testClient))
        .assertNext(value -> {
          SavingsAccount mappedRes = GenericMapper.mapToSpecificClass(value, SavingsAccount.class);
          assertEquals(testSavingsDTO.getAccountType(), mappedRes.getAccountType());
          assertEquals(testSavingsDTO.getClientDocument(), mappedRes.getClientDocument());
          assertEquals(testSavingsDTO.getBalance(), mappedRes.getBalance());
        })
        .verifyComplete();

    verify(saveDomainValidations).validateSavingsAccount(any(Client.class));
    verify(savingsRepo).insert(any(SavingsAccount.class));
  }

  @Test
  void shouldNotSaveSavingsAccount() {
    when(saveDomainValidations.validateSavingsAccount(any(Client.class)))
        .thenReturn(Mono.just(true));

    StepVerifier.create(savingsAccAdapter.save(testSavingsDTO, testClient))
        .verifyError(InvalidRule.class);

    verify(saveDomainValidations).validateSavingsAccount(any(Client.class));
    verify(savingsRepo, never()).insert(any(SavingsAccount.class));
  }

  @Test
  void shouldUpdateSavingsAccountSuccessfully() {
    StepVerifier.create(savingsAccAdapter.update(testSavingsAccount, testSavingsDTO))
        .expectNext("Account updated successfully")
        .verifyComplete();

    verify(updateDomainValidations)
        .validateSavingsAccount(any(SavingsDTO.class), any(SavingsAccount.class));
    verify(updateDomainValidations).validateAmmount(any());
    verify(reactiveMongoTemplate)
        .findAndModify(any(Query.class), any(UpdateDefinition.class), eq(SavingsAccount.class));
  }

  @Test
  void shouldNotUpdateSavingsAccount() {
    when(updateDomainValidations.validateSavingsAccount(any(SavingsDTO.class), any(SavingsAccount.class)))
        .thenReturn(Mono.just(false));

    StepVerifier.create(savingsAccAdapter.update(testSavingsAccount, testSavingsDTO))
        .verifyError(InvalidRule.class);

    verify(updateDomainValidations)
        .validateSavingsAccount(any(SavingsDTO.class), any(SavingsAccount.class));
    verify(updateDomainValidations, never()).validateAmmount(any());
    verify(reactiveMongoTemplate, never())
        .findAndModify(any(Query.class), any(UpdateDefinition.class), eq(SavingsAccount.class));
  }
}