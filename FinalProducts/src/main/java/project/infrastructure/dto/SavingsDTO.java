package project.infrastructure.dto;

import lombok.Data;

/**
 * DTO Cuenta de Ahorros.
 */
@Data
public class SavingsDTO extends BankAccountDTO {
  int movementsLimit;
}
