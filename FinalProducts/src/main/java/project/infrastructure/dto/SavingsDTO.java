package project.infrastructure.dto;

import lombok.Data;

@Data
public class SavingsDTO extends BankAccountDTO {
  int movementsLimit;
}
