package com.microservices_credit.microcervicescredit.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Posibles tipos de errores.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomError {
  private String errorMessage;
  private ErrorType errorType;

  /**
   * Enum que contiene los tipos de errores contemplados.
   */
  public enum ErrorType {
    INVALID_TYPE,
    INVALID_DOCUMENT,
    NOT_FOUND,
    GENERIC_ERROR,
    WRONG_PARAMS
  }
}
