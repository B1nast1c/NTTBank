package project.infrastructure.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO Cuenta de Ahorros.
 */
@Getter
@Setter
public class SavingsDTO extends BankAccountDTO {
  int movementsLimit;
}
