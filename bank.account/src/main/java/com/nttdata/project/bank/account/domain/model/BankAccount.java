package com.nttdata.project.bank.account.domain.model;


import lombok.*;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankAccount {

    @Id
    private String id;
    private String numberAccount;
    private String type;
    private String balance;
    private String numberTransfers;
    private String numberInterbank;
    private String commissionAmount;
    private String headlines;
    private String signatories;
    private String monthlyMovements;
}
