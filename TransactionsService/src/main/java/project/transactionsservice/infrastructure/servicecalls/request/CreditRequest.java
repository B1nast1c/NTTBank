package project.transactionsservice.infrastructure.servicecalls.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreditRequest {
  private boolean paid;
  private double ammount;

  public CreditRequest(double amount) {
    this.ammount = amount;
  }
}
