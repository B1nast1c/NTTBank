package project.transactionsservice.infrastructure.servicecalls.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

// Los nombres de los atributos dependen del microservcio de cuentas bancarias :D

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
  private String accountNumber;
  private String accountType;
  private String clientDocument;
  private double balance;
  private int transactions;
  private double commissionAmount;
  private List<String> accountTitulars;
  private List<Object> legalSigners;
  private Date movementDate;
  private int movementsLimit;
}
