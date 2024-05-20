package com.nttdata.project.bank.account.infrastructure.DTO;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "accounts")
public class BankAccountDTO {

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

    //private String DNI;
}