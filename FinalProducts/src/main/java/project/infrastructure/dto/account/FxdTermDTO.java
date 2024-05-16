package project.infrastructure.dto.account;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FxdTermDTO extends BankAccountDTO {
  LocalDate movementDate = LocalDate.now();
}
