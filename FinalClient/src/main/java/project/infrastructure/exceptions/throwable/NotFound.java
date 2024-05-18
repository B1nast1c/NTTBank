package project.infrastructure.exceptions.throwable;

public class NotFound extends RuntimeException {
  public NotFound(String message) {
    super(message);
  }
}
