package com.nttdata.project.bank.account.Infrastructure.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FixedTermAccount extends BankAccount {
    private String monthlyTransaction;
}
