package project.infrastructure.dto.account;

import lombok.Data;
import project.domain.model.AccountType;

import java.io.Serializable;

@Data
public class BankAccountDTO implements Serializable {
  String clientId;
  String accountNumber;
  double balance;
  int transactions;
  AccountType accountType;
}