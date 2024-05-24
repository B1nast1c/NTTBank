package com.microservices_credit.microcervicescredit.exceptions.throwable;

/**
 * Tipo de cliente que no es PERSONAL o EMPRESARIAL.
 */
public class WrongClientType extends RuntimeException {
  public WrongClientType(String message) {
    super(message);
  }
}
