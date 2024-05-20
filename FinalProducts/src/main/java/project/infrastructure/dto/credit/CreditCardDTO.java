package project.infrastructure.dto.credit;

import lombok.Data;

import java.io.Serializable;

@Data
public class CreditCardDTO implements Serializable {
  String cardNumber;
  String clientId;
  double creditAmmount;
  double allowedCredit;
}