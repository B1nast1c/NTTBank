package project.transactionsservice.infrastructure.servicecalls.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreditResponse {
  private String creditNumber;
  private String clientDocument;
  private String cardNumber;
  private double ammount;
  private boolean paid;
  private double limit;
}
