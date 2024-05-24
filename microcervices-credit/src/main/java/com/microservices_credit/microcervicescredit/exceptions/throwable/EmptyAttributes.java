package com.microservices_credit.microcervicescredit.exceptions.throwable;

/**
 * Los atributos NULOS de alguna manera no son seteados por el usuario.
 */
public class EmptyAttributes extends RuntimeException {
  public EmptyAttributes(String message) {
    super(message);
  }
}
