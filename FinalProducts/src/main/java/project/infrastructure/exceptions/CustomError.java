package project.infrastructure.exceptions;

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
    INVALID_INSTRUCTION,
    INVALID_TYPE,
    NOT_FOUND_CLIENT,
    GENERIC_ERROR,
    WRONG_PARAMS,
    NOT_FOUND_ACCOUNT,
    GET_ERROR,
    POST_ERROR,
    PUT_ERROR,
    DELETE_ERROR
  }
}
