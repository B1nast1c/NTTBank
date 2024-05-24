package project.transactionsservice.infrastructure.strategies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import project.transactionsservice.domain.model.Transaction;
import project.transactionsservice.infrastructure.adapters.mongoRepos.TransactionsRepo;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.exceptions.throwable.InvalidAmmount;
import project.transactionsservice.infrastructure.exceptions.throwable.InvalidClient;
import project.transactionsservice.infrastructure.mapper.GenericMapper;
import project.transactionsservice.infrastructure.servicecalls.responses.AccountResponse;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AccountStrategiesTest {
  private final TransactionDTO transactionDTO = new TransactionDTO(
      "transactionDTO",
      "productNumber",
      "DEPOSITO",
      "2024-01-12",
      "clientNumber",
      "transactionDetail",
      200.50
  );
  private final AccountResponse response = new AccountResponse();
  private final List<Transaction> transactions = new ArrayList<>();
  private Transaction addedTransaction = new Transaction();
  private AccountStrategies accountStrategies;

  @Mock
  private TransactionsRepo transactionsRepo;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    accountStrategies = new AccountStrategies(transactionsRepo);
    transactionDTO.setClientNumber("testNumber");
    response.setClientDocument("testNumber");
  }

  @Test
  void clientShouldBeAllowed() {
    StepVerifier.create(accountStrategies.genericValidation(transactionDTO, response))
        .expectNext(transactionDTO)
        .verifyComplete();
  }

  @Test
  void clientShouldNotBeAllowed() {
    response.setClientDocument("invalidNumber");

    StepVerifier
        .create(accountStrategies.genericValidation(transactionDTO, response))
        .expectError(InvalidClient.class)
        .verify();
  }

  @Test
  void savingsAccountClientShouldBeAllowed() {
    StepVerifier.create(accountStrategies.savingsStrategy(transactionDTO, response))
        .expectNext(transactionDTO)
        .verifyComplete();
  }

  @Test
  void savingsAccountClientShouldNotBeAllowed() {
    response.setClientDocument("invalidNumber");

    StepVerifier.create(accountStrategies.savingsStrategy(transactionDTO, response))
        .expectError(InvalidClient.class)
        .verify();
  }

  @Test
  void fixedTermAccountShouldBeAllowed() {
    when(transactionsRepo.findAllByProductNumber(any(String.class)))
        .thenReturn(Flux.fromIterable(transactions));

    StepVerifier.create(accountStrategies.fxdStrategy(transactionDTO, response))
        .expectNext(transactionDTO)
        .verifyComplete();
  }

  @Test
  void fixedTermAccountShouldNotBeAllowed() {
    addedTransaction = GenericMapper.mapToAny(transactionDTO, Transaction.class);
    transactions.add(addedTransaction);

    when(transactionsRepo.findAllByProductNumber(any(String.class))).thenReturn(Flux.fromIterable(transactions));

    StepVerifier.create(accountStrategies.fxdStrategy(transactionDTO, response))
        .expectError(InvalidAmmount.class) // Monto incorrecto de transacciones
        .verify();
  }

  @Test
  void currentAccountShouldBeAllowedOwner() {
    response.setAccountTitulars(List.of("clientNumber", "otherClient"));

    StepVerifier.create(accountStrategies.currAccStrategy(transactionDTO, response))
        .expectNext(transactionDTO)
        .verifyComplete();
  }

  @Test
  void currentAccountShouldBeAllowedTitulars() {
    response.setClientDocument("anotherNumber");
    response.setAccountTitulars(List.of(transactionDTO.getClientNumber()));

    StepVerifier.create(accountStrategies.currAccStrategy(transactionDTO, response))
        .expectNext(transactionDTO)
        .verifyComplete();
  }

  @Test
  void currentAccountShouldNotBeAllowed() {
    response.setClientDocument("invalidNumber");
    response.setAccountTitulars(List.of("clientNumber", "otherClient", "anotherNumber"));

    StepVerifier.create(accountStrategies.currAccStrategy(transactionDTO, response))
        .expectError(InvalidClient.class)
        .verify();
  }
}
