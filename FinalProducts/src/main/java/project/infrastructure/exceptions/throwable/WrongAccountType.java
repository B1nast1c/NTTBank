package project.infrastructure.exceptions.throwable;

/**
 * Tipo de cuenta que no es AHORRO, CORRIENTE, PLAZO FIJO.
 */
public class WrongAccountType extends RuntimeException {
  public WrongAccountType(String message) {
    super(message);
  }
}
