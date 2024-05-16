package com.nttdata.project.bank.account.Infrastructure.DTO;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrentAccount extends BankAccount{
    private String commissionAmount;
    private String headlines;
    private String signatures;
}
