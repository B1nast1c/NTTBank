package com.nttdata.project.bank.account.Infrastructure.DTO;

import lombok.*;

@Getter
@Setter
//@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount {
    private String numberAccount;
    private String type;
    private String balance;
    private String numberTransfers;
    private String numberInterbank;
    //private String idClient;
}
