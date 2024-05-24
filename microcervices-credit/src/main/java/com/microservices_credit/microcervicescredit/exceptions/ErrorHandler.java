package com.microservices_credit.microcervicescredit.exceptions;

import com.microservices_credit.microcervicescredit.exceptions.throwable.EmptyAttributes;
import com.microservices_credit.microcervicescredit.exceptions.throwable.InvalidDocument;
import com.microservices_credit.microcervicescredit.exceptions.throwable.NotFound;
import com.microservices_credit.microcervicescredit.exceptions.throwable.WrongClientType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Gestión genérica de errores, se incluyen errores originados por la lógica de negocio
 * o errores humanos de ingreso o no ingreso de datos.
 */
@ControllerAdvice
public class ErrorHandler {
  /**
   * Cliente no encontrado.
   *
   * @param e Excepción desencadenada.
   * @return Objeto de error personalizado, que obtiene los datos de un MONO.ERROR.
   */
  @ExceptionHandler(NotFound.class)
  public CustomError handleNotFound(NotFound e) {
    return new CustomError(e.getMessage(), CustomError.ErrorType.NOT_FOUND);
  }

  /**
   * DNI-RUC Vacío o ya presente en la base de datos (MONGO).
   *
   * @param e Excepción desencadenada.
   * @return Objeto de error personalizado, que obtiene los datos de un MONO.ERROR.
   */
  @ExceptionHandler(InvalidDocument.class)
  public CustomError handleInvalidDocument(InvalidDocument e) {
    return new CustomError(e.getMessage(), CustomError.ErrorType.INVALID_DOCUMENT);
  }

  /**
   * Tipo de cliente inválido.
   *
   * @param e Excepción desencadenada.
   * @return Objeto de error personalizado, que obtiene los datos de un MONO.ERROR.
   */
  @ExceptionHandler(WrongClientType.class)
  public CustomError handleWrongClientType(WrongClientType e) {
    return new CustomError(e.getMessage(), CustomError.ErrorType.INVALID_TYPE);
  }

  /**
   * Atributos nulos son nulos, pues el cliente no los establece.
   *
   * @param e Excepción desencadenada.
   * @return Objeto de error personalizado, que obtiene los datos de un MONO.ERROR.
   */
  @ExceptionHandler(EmptyAttributes.class)
  public CustomError handleEmptyAttributes(EmptyAttributes e) {
    return new CustomError(e.getMessage(), CustomError.ErrorType.GENERIC_ERROR);
  }

  /**
   * Handle Excepción genérica.
   *
   * @param e Excepción desencadenada
   * @return Objeto de error personalizado, que obtiene los datos de un MONO.ERROR
   */
  @ExceptionHandler(IllegalStateException.class)
  public CustomError handleIllegalStateException(IllegalStateException e) {
    return new CustomError("Could not find client", CustomError.ErrorType.NOT_FOUND);
  }
}
