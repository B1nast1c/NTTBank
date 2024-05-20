package project.transactionsservice.infrastructure.serviceCalls.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

// Los nombres de los atributos dependen del microservcio de cuentas bancarias :D

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
  private String accountNumber;
  private String type;
  private double balance;
  private int transactions;
  private String numberInterbank;
  private double commissionAmount;
  private List<String> headlines;
  private List<?> signatories;
  private int monthlyMovements;
}
