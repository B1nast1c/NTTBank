package project.infrastructure.dto;

import lombok.Data;

import java.util.Date;

/**
 * DTO de Cuenta a Plazo Fijo.
 */
@Data
public class FxdTermDTO extends BankAccountDTO {
  Date movementDate = new Date();
}
