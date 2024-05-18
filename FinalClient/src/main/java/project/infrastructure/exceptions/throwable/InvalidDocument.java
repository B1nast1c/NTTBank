package project.infrastructure.exceptions.throwable;

public class InvalidDocument extends RuntimeException {
  public InvalidDocument(String message) {
    super(message);
  }
}
