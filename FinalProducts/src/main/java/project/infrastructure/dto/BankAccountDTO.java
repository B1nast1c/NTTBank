package project.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountDTO implements Serializable {
  String id;
  String clientDocument;
  String accountNumber;
  double balance;
  int transactions;
  String accountType;
}