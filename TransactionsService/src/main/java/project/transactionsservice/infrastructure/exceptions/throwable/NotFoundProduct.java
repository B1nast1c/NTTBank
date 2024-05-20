package project.transactionsservice.infrastructure.exceptions.throwable;

public class NotFoundProduct extends RuntimeException {
  public NotFoundProduct(String message) {
    super(message);
  }
}
