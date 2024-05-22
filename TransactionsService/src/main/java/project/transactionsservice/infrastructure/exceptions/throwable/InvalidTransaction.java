package project.transactionsservice.infrastructure.exceptions.throwable;

public class InvalidTransaction extends RuntimeException {
  public InvalidTransaction(String message) {
    super(message);
  }
}
