package project.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO de cuenta bancaria.
 */
@Getter
@Setter
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