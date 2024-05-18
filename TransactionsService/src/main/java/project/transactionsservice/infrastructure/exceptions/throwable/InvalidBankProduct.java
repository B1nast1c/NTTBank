package project.transactionsservice.infrastructure.exceptions.throwable;

public class InvalidBankProduct extends RuntimeException { // Transacción incorrecta a cuenta incorrecta
  public InvalidBankProduct(String message) {
    super(message);
  }
}
