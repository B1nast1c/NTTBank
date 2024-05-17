package com.nttdata.project.bank.account.Infrastructure.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavingsAccount extends BankAccount {

    private String monthlyMovements;

}
