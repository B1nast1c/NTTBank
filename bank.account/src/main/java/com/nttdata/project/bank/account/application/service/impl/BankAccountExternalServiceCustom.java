package com.nttdata.project.bank.account.application.service.impl;

import com.nttdata.project.bank.account.application.service.BankAccountExternalService;
import com.nttdata.project.bank.account.infrastructure.DTO.BankAccountDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BankAccountExternalServiceCustom implements com.nttdata.project.bank.account.application.service.BankAccountExternalServiceCustom {

    private final BankAccountExternalService bankAccountExternalService;

    @Override
    public void saveBankAccount(BankAccountDTO bankAccountDTO) {
        bankAccountExternalService.saveBankAccount(bankAccountDTO);
    }
}
