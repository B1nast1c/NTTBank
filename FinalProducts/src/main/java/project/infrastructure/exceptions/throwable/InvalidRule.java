package project.infrastructure.exceptions.throwable;

/**
 * Número del documento VACÍO o DUPLICADO, al momento de ingresar.
 */
public class InvalidRule extends RuntimeException {
  public InvalidRule(String message) {
    super(message);
  }
}
