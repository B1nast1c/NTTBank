package project.transactionsservice.infrastructure.serviceCalls.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Los nombres de los atributos dependen del microservcio de cuentas bancarias :D

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
  double balance;
  String clientNumber;
}
