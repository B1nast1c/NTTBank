package com.nttdata.project.bank.account.application.service.impl;

import com.nttdata.project.bank.account.application.service.BankAccountExternalService;
import com.nttdata.project.bank.account.infrastructure.dto.BankAccountDTO;
import org.springframework.stereotype.Service;

@Service
public class BankAccountExternalServiceCustom implements com.nttdata.project.bank.account.application.service.BankAccountExternalServiceCustom {

  private final BankAccountExternalService bankAccountExternalService;

  public BankAccountExternalServiceCustom(BankAccountExternalService bankAccountExternalService) {
    this.bankAccountExternalService = bankAccountExternalService;
  }

  @Override
  public void saveBankAccount(BankAccountDTO bankAccountDTO) {
    bankAccountExternalService.saveBankAccount(bankAccountDTO);
  }
}
