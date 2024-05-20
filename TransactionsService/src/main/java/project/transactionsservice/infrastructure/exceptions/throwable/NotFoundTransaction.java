package project.transactionsservice.infrastructure.exceptions.throwable;

public class NotFoundTransaction extends RuntimeException {
  public NotFoundTransaction(String message) {
    super(message);
  }
}
