package com.microservices_credit.microcervicescredit.validations;

import com.microservices_credit.microcervicescredit.apicalls.ClientResponse;
import com.microservices_credit.microcervicescredit.entity.Credits;
import com.microservices_credit.microcervicescredit.exceptions.throwable.InvalidDocument;

import java.util.List;

public class CreditValidations {
  private CreditValidations() {
  }

  public static void validateClient(ClientResponse response) {
    if (!response.isSuccess()) {
      throw new InvalidDocument("The client with the given document is not valid");
    }
  }

  public static void validateCreditsAmmount(Credits credit, List<Credits> foundCredits, String clientType) {
    boolean hasCredits = foundCredits
        .stream()
        .anyMatch(
            foundCredit -> foundCredit.getClientDocument().equals(credit.getClientDocument())
        );
    if (hasCredits && clientType.equals("PERSONAL")) throw new InvalidDocument("The client already has a credit");
  }

  public static void validateCreditCard(boolean hasCreditCard) {
    if (hasCreditCard) throw new InvalidDocument("The client already has a credit card");
  }
}
