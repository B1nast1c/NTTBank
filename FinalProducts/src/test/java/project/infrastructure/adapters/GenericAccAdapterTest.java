package project.infrastructure.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import project.domain.model.account.CurrentAccount;
import project.domain.model.account.FixedTermAccount;
import project.domain.model.account.SavingsAccount;
import project.infrastructure.adapters.mongorepo.CurrAccRepo;
import project.infrastructure.adapters.mongorepo.FxdTermRepo;
import project.infrastructure.adapters.mongorepo.SavingsRepo;
import project.infrastructure.dto.CurrAccDTO;
import project.infrastructure.dto.FxdTermDTO;
import project.infrastructure.dto.SavingsDTO;
import project.infrastructure.mapper.GenericMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class GenericAccAdapterTest {
  private final FixedTermAccount testFixed = new FixedTermAccount();
  private final SavingsAccount testSavings = new SavingsAccount();
  private final CurrentAccount testCurrent = new CurrentAccount();
  List<Object> allAccounts = new ArrayList<>();

  @InjectMocks
  private GenericAccAdapter genericAccAdapter;

  @Mock
  private CurrAccRepo currAccRepo;

  @Mock
  private FxdTermRepo fxdTermRepo;

  @Mock
  private SavingsRepo savingsRepo;

  @Mock
  private GenericMapper genericMapper;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    testCurrent.setId("currentID");
    testFixed.setId("fixedID");
    testSavings.setId("savingsID");
  }

  @Test
  void shouldFindNoAccounts() {
    when(currAccRepo.findAll()).thenReturn(Flux.empty());
    when(fxdTermRepo.findAll()).thenReturn(Flux.empty());
    when(savingsRepo.findAll()).thenReturn(Flux.empty());

    StepVerifier.create(genericAccAdapter.findAll()).expectComplete().verify();
  }

  @Test
  void shouldReturnOneOfEachAccount() {
    when(currAccRepo.findAll()).thenReturn(Flux.just(testCurrent));
    when(fxdTermRepo.findAll()).thenReturn(Flux.just(testFixed));
    when(savingsRepo.findAll()).thenReturn(Flux.just(testSavings));

    StepVerifier.create(genericAccAdapter.findAll())
        .expectNext(GenericMapper.mapToSpecificClass(testCurrent, CurrAccDTO.class))
        .expectNext(GenericMapper.mapToSpecificClass(testFixed, FxdTermDTO.class))
        .expectNext(GenericMapper.mapToSpecificClass(testSavings, SavingsDTO.class))
        .expectComplete()
        .verify();
  }

  @Test
  void shouldFindNoAccountsForClient() {
    String clientId = "123";
    when(currAccRepo.findAllByClientDocument(clientId)).thenReturn(Flux.empty());
    when(fxdTermRepo.findAllByClientDocument(clientId)).thenReturn(Flux.empty());
    when(savingsRepo.findAllByClientDocument(clientId)).thenReturn(Flux.empty());

    StepVerifier.create(genericAccAdapter.findByClientId(clientId)).expectComplete().verify();
  }

  @Test
  void shouldThrowNotFoundException() {
    String accountNumber = "123";
    when(currAccRepo.findByAccountNumber(accountNumber)).thenReturn(Mono.empty());
    when(fxdTermRepo.findByAccountNumber(accountNumber)).thenReturn(Mono.empty());
    when(savingsRepo.findByAccountNumber(accountNumber)).thenReturn(Mono.empty());

    StepVerifier
        .create(genericAccAdapter.findByAccountNumber(accountNumber))
        .expectError(NoSuchElementException.class)
        .verify();
  }

  @Test
  void shouldReturnCurrentAccount() {
    String accountNumber = "123";
    CurrentAccount currentAccount = new CurrentAccount();
    when(currAccRepo.findByAccountNumber(any(String.class))).thenReturn(Mono.just(currentAccount));

    StepVerifier
        .create(genericAccAdapter.findByAccountNumber(accountNumber))
        .expectNext(currentAccount)
        .expectComplete()
        .verify();
  }

  @Test
  void shouldReturnFixedTermAccount() {
    String accountNumber = "123";
    FixedTermAccount fxdTermAccount = new FixedTermAccount();
    when(fxdTermRepo.findByAccountNumber(any(String.class))).thenReturn(Mono.just(fxdTermAccount));

    StepVerifier
        .create(fxdTermRepo.findByAccountNumber(accountNumber))
        .expectNext(fxdTermAccount)
        .expectComplete()
        .verify();
  }

  @Test
  void findByAccountNumber_SavingsAccount_ReturnsMonoWithAccount() {
    String accountNumber = "123";
    SavingsAccount savingsAccount = new SavingsAccount();
    when(savingsRepo.findByAccountNumber(any(String.class))).thenReturn(Mono.just(savingsAccount));

    StepVerifier
        .create(savingsRepo.findByAccountNumber(accountNumber))
        .expectNext(savingsAccount)
        .expectComplete()
        .verify();
  }
}
