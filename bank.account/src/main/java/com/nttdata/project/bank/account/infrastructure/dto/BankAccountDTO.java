package com.nttdata.project.bank.account.infrastructure.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountDTO {
  private String accountNumber;
  private String type;
  private double balance = 0;
  private int transactions = 0;
  private String numberInterbank;
  private double commissionAmount = 0.0;
  private List<String> headlines = new ArrayList<>();
  private List<?> signatories = new ArrayList<>();
  private int monthlyMovements;
}
