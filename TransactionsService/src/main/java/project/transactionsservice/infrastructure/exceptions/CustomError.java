package project.transactionsservice.infrastructure.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Posibles tipos de errores.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomError {
  private String errorMessage;
  private ErrorType errorType;

  /**
   * Enum que contiene los tipos de errores contemplados.
   */
  public enum ErrorType {
    INVALID_AMMOUNT,
    ACCOUNT_NOT_FOUND,
    TRANSACTION_NOT_FOUND,
    INVALID_BANK_PRODUCT,
    EMPTY_ATTRIBUTES,
    INVALID_CLIENT,
    GENERIC_ERROR,
    GETTING_ERROR,
    POSTING_ERROR,
    INVALID_TRANSACTION
  }
}
