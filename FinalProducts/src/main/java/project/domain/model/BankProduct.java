package project.domain.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Clase padre de las cuentas bancarias.
 */
@Getter
@Setter
public class BankProduct {
  /**
   * DNI o RUC del cliente.
   */
  protected String clientDocument;
}
