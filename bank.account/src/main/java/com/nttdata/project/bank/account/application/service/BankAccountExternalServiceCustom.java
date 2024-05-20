package com.nttdata.project.bank.account.application.service;

import com.nttdata.project.bank.account.domain.model.BankAccount;
import com.nttdata.project.bank.account.infrastructure.DTO.BankAccountDTO;

public interface BankAccountExternalServiceCustom {

    void saveBankAccount(BankAccountDTO bankAccountDTO);
}
