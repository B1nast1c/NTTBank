package project.infrastructure.exceptions.throwable;

/**
 * Cliente no encontrado.
 */
public class NotFound extends RuntimeException {
  public NotFound(String message) {
    super(message);
  }
}
