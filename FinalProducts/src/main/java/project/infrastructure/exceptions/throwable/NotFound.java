package project.infrastructure.exceptions.throwable;

/**
 * Cuenta Bancaria no encontrada.
 */
public class NotFound extends RuntimeException {
  public NotFound(String message) {
    super(message);
  }
}
