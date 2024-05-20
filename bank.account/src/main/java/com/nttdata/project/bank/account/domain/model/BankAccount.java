package com.nttdata.project.bank.account.domain.model;


import lombok.*;
import org.springframework.data.annotation.Id;

import javax.annotation.PostConstruct;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount {
  @Id
  private String id;

  private String accountNumber; // Elemento generado, no brindado por el usuario
  private String type; // Recomiento hacer un enum, que corresponde exactamente a los tipos de cuenta proporcionados por el documento
  private double balance = 0;
  private int transactions = 0;
  private String numberInterbank;
  private double commissionAmount = 0.0; // Recomiendo hacer herencia, por que este elemento NO lo comparten los tres tipos de cuenta
  private List<String> headlines = new ArrayList<>(); // Debe ser una lista, con los IDS de los clientes que van a ser titulares, validar minumo uno al crear (CUENTA CORRIENTE)
  private List<?> signatories = new ArrayList<>(); // Manejalo como veas conveniente, pero corresponde a una lista
  private int monthlyMovements;

  // Falta la fecha maxima de movimiento mensual para la cuenta a plazo fijo

  @PostConstruct
  private void initialize() {
    this.accountNumber = generateAccountNumber();
  }

  private String generateAccountNumber() {
    SecureRandom random = new SecureRandom();
    return IntStream.range(0, 16).mapToObj(i -> String.valueOf(random.nextInt(10))).collect(Collectors.joining());
  }
}
