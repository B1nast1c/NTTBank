package project.transactionsservice.infrastructure.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import project.transactionsservice.infrastructure.exceptions.throwable.*;

@ControllerAdvice
public class ErrorHandler {
  @ExceptionHandler(NotFoundProduct.class)
  public CustomError handleNotFoundAccount(NotFoundProduct e) {
    return new CustomError(e.getMessage(), CustomError.ErrorType.ACCOUNT_NOT_FOUND);
  }

  @ExceptionHandler(NotFoundTransaction.class)
  public CustomError handleNotFoundTransaction(NotFoundTransaction e) {
    return new CustomError(e.getMessage(), CustomError.ErrorType.TRANSACTION_NOT_FOUND);
  }

  @ExceptionHandler(InvalidBankProduct.class)
  public CustomError handleInvalidBankProduct(InvalidBankProduct e) {
    return new CustomError(e.getMessage(), CustomError.ErrorType.INVALID_BANK_PRODUCT);
  }

  @ExceptionHandler(DefersAmmount.class)
  public CustomError handleWrongClientType(DefersAmmount e) {
    return new CustomError(e.getMessage(), CustomError.ErrorType.INVALID_AMMOUNT);
  }

  @ExceptionHandler(EmptyAttributes.class)
  public CustomError handleEmptyAttributes(EmptyAttributes e) {
    return new CustomError(e.getMessage(), CustomError.ErrorType.EMPTY_ATTRIBUTES);
  }

  @ExceptionHandler(IllegalStateException.class)
  public CustomError handleGenericException(IllegalStateException e) {
    return new CustomError("An error has occurred", CustomError.ErrorType.GENERIC_ERROR);
  }
}
