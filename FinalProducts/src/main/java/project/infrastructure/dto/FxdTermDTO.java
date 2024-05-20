package project.infrastructure.dto;

import lombok.Data;

import java.util.Date;

@Data
public class FxdTermDTO extends BankAccountDTO {
  Date movementDate = new Date();
}
