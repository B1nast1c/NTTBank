package com.microservices_credit.microcervicescredit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "creditcards")
public class CreditCard {
  @Id
  private String id;
  private String cardNumber;
  private String clientDocument;
  private double ammount = 0; // Cuanto dinero me queda en la tarjeta
  private double limit;

  public String generateCardNumber() {
    SecureRandom random = new SecureRandom();
    return "CREDITCARD_" + IntStream.range(0, 10)
        .mapToObj(i -> String.valueOf(random.nextInt(10)))
        .collect(Collectors.joining());
  }
}
