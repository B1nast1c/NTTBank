package project.transactionsservice.infrastructure.exceptions.throwable;

public class DefersAmmount extends RuntimeException { // Operación excedente de saldo
  public DefersAmmount(String message) {
    super(message);
  }
}
