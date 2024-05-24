package com.microservices_credit.microcervicescredit.exceptions.throwable;

/**
 * Cliente no encontrado.
 */
public class NotFound extends RuntimeException {
  public NotFound(String message) {
    super(message);
  }
}
