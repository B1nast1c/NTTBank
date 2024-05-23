package project.infrastructure.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * DTO de Cuenta a Plazo Fijo.
 */
@Getter
@Setter
public class FxdTermDTO extends BankAccountDTO {
  Date movementDate = new Date();
}
