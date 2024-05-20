package com.nttdata.project.bank.account.application.service;

import com.nttdata.project.bank.account.infrastructure.dto.BankAccountDTO;

public interface BankAccountExternalServiceCustom {

  void saveBankAccount(BankAccountDTO bankAccountDTO);
}
