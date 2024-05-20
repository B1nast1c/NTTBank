package project.transactionsservice.infrastructure.exceptions.throwable;

public class InvalidTransactionType extends RuntimeException {
  public InvalidTransactionType(String message) {
    super(message);
  }
}
