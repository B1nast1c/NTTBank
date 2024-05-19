package project.infrastructure.exceptions.throwable;

/**
 * Número del documento VACÍO o DUPLICADO, al momento de ingresar.
 */
public class InvalidDocument extends RuntimeException {
  public InvalidDocument(String message) {
    super(message);
  }
}
