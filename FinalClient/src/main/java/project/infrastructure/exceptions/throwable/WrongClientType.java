package project.infrastructure.exceptions.throwable;

public class WrongClientType extends RuntimeException {
  public WrongClientType(String message) {
    super(message);
  }
}
