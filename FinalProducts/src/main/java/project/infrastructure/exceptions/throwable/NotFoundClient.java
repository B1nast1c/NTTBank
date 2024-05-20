package project.infrastructure.exceptions.throwable;

/**
 * Cliente no encontrado.
 */
public class NotFoundClient extends RuntimeException {
  public NotFoundClient(String message) {
    super(message);
  }
}
