package project.transactionsservice.application.validations;

import project.transactionsservice.domain.model.TransactionType;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.exceptions.throwable.InvalidAmmount;
import project.transactionsservice.infrastructure.exceptions.throwable.InvalidTransactionType;
import project.transactionsservice.infrastructure.exceptions.throwable.NotFoundProduct;
import project.transactionsservice.infrastructure.serviceCalls.responses.AccountResponse;
import project.transactionsservice.infrastructure.serviceCalls.responses.GenericResponse;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class TransactionsAppValidations {

  private static final List<String> accountTypes = List.of("CUENTA_CORRIENTE", "AHORRO", "PLAZO_FIJO");

  public static Mono<TransactionDTO> validateTransactionType(TransactionDTO transaction) {
    try {
      TransactionType.valueOf(transaction.getTransactionType());
      return Mono.just(transaction);
    }
    catch (Exception e) {
      return Mono.error(new InvalidTransactionType("The transaction type is not valid"));
    }
  }

  public static Mono<TransactionDTO> validateProduct(TransactionDTO transaction, GenericResponse serviceResponse) {
    boolean isValidProduct = serviceResponse.isSuccess();
    if (isValidProduct) {
      return Mono.just(transaction);
    }
    return Mono.error(new NotFoundProduct("The Account/Credit/Credit Card does not exist"));
  }
}
