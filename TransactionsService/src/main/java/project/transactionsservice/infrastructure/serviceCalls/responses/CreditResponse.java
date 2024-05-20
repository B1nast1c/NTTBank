package project.transactionsservice.infrastructure.serviceCalls.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreditResponse {
  private double ammount; // Depende del microservicio de creditos
}
