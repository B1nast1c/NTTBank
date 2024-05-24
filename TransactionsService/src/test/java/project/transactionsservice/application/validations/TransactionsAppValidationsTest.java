package project.transactionsservice.application.validations;

import org.junit.jupiter.api.Test;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.exceptions.throwable.InvalidTransactionType;
import project.transactionsservice.infrastructure.exceptions.throwable.NotFoundProduct;
import project.transactionsservice.infrastructure.servicecalls.responses.AccountResponse;
import project.transactionsservice.infrastructure.servicecalls.responses.GenericResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class TransactionsAppValidationsTest {
  @Test
  void transactionTypeShouldBeValid() {
    TransactionDTO transaction = new TransactionDTO();
    transaction.setTransactionType("DEPOSITO");

    Mono<TransactionDTO> result = TransactionsAppValidations
        .validateTransactionType(transaction);

    StepVerifier.create(result)
        .expectNext(transaction)
        .verifyComplete();
  }

  @Test
  void transactionTypeShouldNotBeValid() {
    TransactionDTO transaction = new TransactionDTO();
    transaction.setTransactionType("INVALID_TYPE");

    Mono<TransactionDTO> result = TransactionsAppValidations
        .validateTransactionType(transaction);

    StepVerifier.create(result)
        .expectError(InvalidTransactionType.class)
        .verify();
  }

  @Test
  void bankProductShouldBeValid() {
    TransactionDTO transaction = new TransactionDTO();
    GenericResponse serviceResponse = new GenericResponse(true, new AccountResponse());

    Mono<TransactionDTO> result = TransactionsAppValidations
        .validateProduct(transaction, serviceResponse);

    StepVerifier.create(result)
        .expectNext(transaction)
        .verifyComplete();
  }

  @Test
  void bankProductShouldNotBeValid() {
    TransactionDTO transaction = new TransactionDTO();
    GenericResponse serviceResponse = new GenericResponse(false, new AccountResponse());

    Mono<TransactionDTO> result = TransactionsAppValidations
        .validateProduct(transaction, serviceResponse);

    StepVerifier.create(result)
        .expectError(NotFoundProduct.class)
        .verify();
  }
}