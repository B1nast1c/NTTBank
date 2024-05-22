package project.infrastructure.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import project.infrastructure.exceptions.throwable.*;

/**
 * Clase para gestionar errores de manera genérica en toda la aplicación.
 * Se incluyen errores originados por la lógica de negocio o por errores
 * de ingreso o no ingreso de datos por parte del usuario.
 */
@ControllerAdvice
public class ErrorHandler {
  /**
   * Maneja la excepción cuando no se encuentra un cliente.
   *
   * @param e La excepción NotFoundClient lanzada.
   * @return Un objeto CustomError con el mensaje de error y el tipo de error correspondiente.
   */
  @ExceptionHandler(NotFoundClient.class)
  public CustomError handleNotFound(NotFoundClient e) {
    return new CustomError(e.getMessage(), CustomError.ErrorType.NOT_FOUND_CLIENT);
  }

  /**
   * Maneja la excepción cuando no se encuentra una cuenta.
   *
   * @param e La excepción NotFound lanzada.
   * @return Un objeto CustomError con el mensaje de error y el tipo de error correspondiente.
   */
  @ExceptionHandler(NotFound.class)
  public CustomError handleNotAccount(NotFound e) {
    return new CustomError(e.getMessage(), CustomError.ErrorType.NOT_FOUND_ACCOUNT);
  }

  /**
   * Maneja la excepción cuando se encuentra un documento inválido.
   *
   * @param e La excepción InvalidRule lanzada.
   * @return Un objeto CustomError con el mensaje de error y el tipo de error correspondiente.
   */
  @ExceptionHandler(InvalidRule.class)
  public CustomError handleInvalidDocument(InvalidRule e) {
    return new CustomError(e.getMessage(), CustomError.ErrorType.INVALID_INSTRUCTION);
  }

  /**
   * Maneja la excepción cuando se encuentra un tipo de cliente incorrecto.
   *
   * @param e La excepción WrongClientType lanzada.
   * @return Un objeto CustomError con el mensaje de error y el tipo de error correspondiente.
   */
  @ExceptionHandler(WrongClientType.class)
  public CustomError handleWrongClientType(WrongClientType e) {
    return new CustomError(e.getMessage(), CustomError.ErrorType.INVALID_TYPE);
  }

  /**
   * Maneja la excepción cuando se encuentra un tipo de cuenta incorrecto.
   *
   * @param e La excepción WrongAccountType lanzada.
   * @return Un objeto CustomError con el mensaje de error y el tipo de error correspondiente.
   */
  @ExceptionHandler(WrongAccountType.class)
  public CustomError handleWrongAccountType(WrongAccountType e) {
    return new CustomError(e.getMessage(), CustomError.ErrorType.INVALID_TYPE);
  }

  /**
   * Maneja la excepción cuando se encuentran atributos nulos.
   *
   * @param e La excepción EmptyAttributes lanzada.
   * @return Un objeto CustomError con el mensaje de error y el tipo de error correspondiente.
   */
  @ExceptionHandler(EmptyAttributes.class)
  public CustomError handleEmptyAttributes(EmptyAttributes e) {
    return new CustomError(e.getMessage(), CustomError.ErrorType.GENERIC_ERROR);
  }

  /**
   * Maneja la excepción genérica IllegalStateException.
   *
   * @param e La excepción IllegalStateException lanzada.
   * @return Un objeto CustomError con el mensaje de error y el tipo de error correspondiente.
   */
  @ExceptionHandler(IllegalStateException.class)
  public CustomError handleIllegalStateException(IllegalStateException e) {
    return new CustomError("No se pudo encontrar el cliente", CustomError.ErrorType.GENERIC_ERROR);
  }
}