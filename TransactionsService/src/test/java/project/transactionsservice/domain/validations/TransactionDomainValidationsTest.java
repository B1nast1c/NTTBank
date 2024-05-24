package project.transactionsservice.domain.validations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.transactionsservice.domain.model.Transaction;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.exceptions.throwable.InvalidAmmount;
import project.transactionsservice.infrastructure.exceptions.throwable.InvalidTransaction;
import project.transactionsservice.infrastructure.servicecalls.request.CreditRequest;
import project.transactionsservice.infrastructure.servicecalls.responses.AccountResponse;
import project.transactionsservice.infrastructure.servicecalls.responses.CreditResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

// Implementar validaciones de credito + tarjeta de credito

class TransactionDomainValidationsTest {
  private final TransactionDTO transaction = new TransactionDTO();

  @BeforeEach
  void setUp() {
    transaction.setProductNumber("testProductNumber");
    transaction.setTransactionDate("2022-05-01");
  }

  @Test
  void transactionFixedAccountShouldPass() {
    List<Transaction> transactions = Collections.emptyList();

    Mono<TransactionDTO> result = TransactionDomainValidations
        .validateFxdAccount(transaction, transactions);

    StepVerifier.create(result)
        .expectNext(transaction)
        .verifyComplete();
  }

  @Test
  void transactionFixedAccountShouldFail() { // Dentro del mismo mes
    transaction.setTransactionDate("2024-05-31");
    Transaction addedTransaction = new Transaction();
    addedTransaction.setProductNumber("testProductNumber");
    addedTransaction.setTransactionDate(new Date());
    List<Transaction> transactions = Collections.singletonList(addedTransaction);

    Mono<TransactionDTO> result = TransactionDomainValidations
        .validateFxdAccount(transaction, transactions);

    StepVerifier.create(result)
        .expectError(InvalidAmmount.class)
        .verify();
  }

  @Test
  void transactionFixedAccountShouldPassByAccountNumber() { // Dentro del mismo mes
    transaction.setTransactionDate("2024-01-31");
    Transaction addedTransaction = new Transaction();
    addedTransaction.setProductNumber("anotherNumber");
    addedTransaction.setTransactionDate(new Date());
    List<Transaction> transactions = Collections.singletonList(addedTransaction);

    Mono<TransactionDTO> result = TransactionDomainValidations
        .validateFxdAccount(transaction, transactions);

    StepVerifier.create(result)
        .expectNext(transaction)
        .verifyComplete();
  }

  @Test
  void isSameYearAndMonthShouldThrowException() {
    String invalidDate = "invalid-date";
    Date validDate = new Date();

    assertThrows(InvalidTransaction.class, () ->
        TransactionDomainValidations.isSameYearAndMonth(invalidDate, validDate)
    );
  }

  @Test
  void transactionFixedAccountShouldBeValidByYear() { // Dentro del mismo mes
    transaction.setTransactionDate("2023-12-12"); // Intercambio mes y día
    Transaction addedTransaction = new Transaction();
    addedTransaction.setProductNumber("testProductNumber");
    addedTransaction.setTransactionDate(new Date());
    List<Transaction> transactions = Collections.singletonList(addedTransaction);

    Mono<TransactionDTO> result = TransactionDomainValidations
        .validateFxdAccount(transaction, transactions);

    StepVerifier.create(result)
        .expectNext(transaction)
        .verifyComplete();
  }

  @Test
  void transactionFixedAccountShouldBeValidByMonth() { // Dentro del mismo mes
    transaction.setTransactionDate("2024-06-07"); // Intercambio mes y día
    Transaction addedTransaction = new Transaction();
    addedTransaction.setProductNumber("testProductNumber");
    addedTransaction.setTransactionDate(new Date());
    List<Transaction> transactions = Collections.singletonList(addedTransaction);

    Mono<TransactionDTO> result = TransactionDomainValidations
        .validateFxdAccount(transaction, transactions);

    StepVerifier.create(result)
        .expectNext(transaction)
        .verifyComplete();
  }

  @Test
  void depositValidTransaction() {
    TransactionDTO transaction = new TransactionDTO();
    transaction.setAmmount(50000.0);

    Mono<TransactionDTO> result = TransactionDomainValidations.validateDeposit(transaction);

    StepVerifier.create(result)
        .expectNext(transaction)
        .verifyComplete();
  }

  @Test
  void depositShouldNotBeValid() {
    TransactionDTO transaction = new TransactionDTO();
    transaction.setAmmount(200000.0);

    Mono<TransactionDTO> result = TransactionDomainValidations.validateDeposit(transaction);

    StepVerifier.create(result)
        .expectError(InvalidAmmount.class)
        .verify();
  }

  @Test
  void withdrawalShouldBeValid() {
    TransactionDTO transaction = new TransactionDTO();
    transaction.setAmmount(50000.0);
    AccountResponse account = new AccountResponse();
    account.setBalance(100000.0);

    Mono<TransactionDTO> result = TransactionDomainValidations.validateWithdrawal(transaction, account);

    StepVerifier.create(result)
        .expectNext(transaction)
        .verifyComplete();
  }

  @Test
  void withdrawalShouldNoteBeValid() {
    TransactionDTO transaction = new TransactionDTO();
    transaction.setAmmount(200000.0);
    AccountResponse account = new AccountResponse();
    account.setBalance(100000.0);

    Mono<TransactionDTO> result = TransactionDomainValidations.validateWithdrawal(transaction, account);

    StepVerifier.create(result)
        .expectError(InvalidAmmount.class)
        .verify();
  }

  @Test
  void creditPaymentShouldBeValid() {
    TransactionDTO transaction = new TransactionDTO();
    transaction.setAmmount(50000.0);
    CreditRequest credit = new CreditRequest();
    credit.setAmmount(50000.0);

    Mono<TransactionDTO> result = TransactionDomainValidations.validateCreditPayment(transaction);

    StepVerifier.create(result)
        .expectNext(transaction)
        .verifyComplete();
  }
  
  @Test
  void creditCardChargeShouldNotBeValid() {
    TransactionDTO transaction = new TransactionDTO();
    transaction.setAmmount(200000.0);
    CreditResponse creditCard = new CreditResponse();
    creditCard.setAmmount(100000.0);

    Mono<TransactionDTO> result = TransactionDomainValidations.validateCreditCardCharge(transaction, creditCard);

    StepVerifier.create(result)
        .expectError(InvalidAmmount.class)
        .verify();
  }
}