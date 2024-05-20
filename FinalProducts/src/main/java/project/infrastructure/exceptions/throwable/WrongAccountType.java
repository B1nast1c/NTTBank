package project.infrastructure.exceptions.throwable;

/**
 * Tipo de cliente que no es PERSONAL o EMPRESARIAL.
 */
public class WrongAccountType extends RuntimeException {
  public WrongAccountType(String message) {
    super(message);
  }
}
