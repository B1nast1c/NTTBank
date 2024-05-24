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

/**
 * @Autor: Bryan Flores
 * Tarea: Microservicios de creditos
 * Fecha: 9/05/2024
 */


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "creditss")
public class Credits {
  @Id
  private String id;
  private String creditNumber;
  private String creditType;
  private String clientDocument;
  private double ammount;
  private boolean paid = false;

  public String generateCreditNumber() {
    SecureRandom random = new SecureRandom();
    return "CREDIT_" + IntStream.range(0, 10)
        .mapToObj(i -> String.valueOf(random.nextInt(10)))
        .collect(Collectors.joining());
  }
}
