package project.domain.model;

import lombok.Data;

/**
 * Clase padre de las cuentas bancarias.
 */
@Data
public class BankProduct {
  /**
   * DNI o RUC del cliente.
   */
  protected String clientDocument;
}
