package project.transactionsservice.application.validations;

import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.exceptions.throwable.InvalidAmmount;
import project.transactionsservice.infrastructure.exceptions.throwable.NotFoundProduct;
import project.transactionsservice.infrastructure.serviceCalls.responses.GenericResponse;
import reactor.core.publisher.Mono;

public class TransactionsAppValidations {
  public static Mono<TransactionDTO> validateTransactionType(TransactionDTO transaction) {
    if (transaction.getAmount() <= 100000) {
      return Mono.just(transaction);
    }
    return Mono.error(new InvalidAmmount("The deposit ammount is too big"));
  }

  public static Mono<TransactionDTO> validateProduct(TransactionDTO transaction, GenericResponse serviceResponse) {
    boolean isValidProduct = serviceResponse.isSuccess();
    if (isValidProduct) {
      return Mono.just(transaction);
    }
    return Mono.error(new NotFoundProduct("The Account/Credit/Credit Card does not exist"));
  }
}
