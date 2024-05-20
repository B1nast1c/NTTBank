package project.transactionsservice.infrastructure.exceptions.throwable;

public class InvalidAmmount extends RuntimeException { // Operación excedente de saldo
  public InvalidAmmount(String message) {
    super(message);
  }
}
