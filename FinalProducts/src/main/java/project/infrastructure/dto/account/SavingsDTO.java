package project.infrastructure.dto.account;

import lombok.Data;

@Data
public class SavingsDTO extends BankAccountDTO {
  int movementsLimit;
}
