package project.transactionsservice.infrastructure.exceptions.throwable;

public class EmptyAttributes extends RuntimeException {
  public EmptyAttributes(String message) {
    super(message);
  }
}
