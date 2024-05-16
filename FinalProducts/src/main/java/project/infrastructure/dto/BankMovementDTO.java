package project.infrastructure.dto;

import lombok.Data;
import project.domain.model.Account.BankAccount;
import project.domain.model.Credits.Credit;
import project.domain.model.MovementType;

import java.io.Serializable;
import java.util.Date;

@Data
public class BankMovementDTO implements Serializable {
  String movementId;
  Credit credit;
  BankAccount bankAccount;
  MovementType movementType;
  double movementAmmount;
  Date movementDate;
}