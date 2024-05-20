package com.nttdata.project.bank.account.application.service.impl;

import com.nttdata.project.bank.account.application.service.BankAccountService;
import com.nttdata.project.bank.account.infrastructure.dto.BankAccountDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class BankAccountExternalService implements com.nttdata.project.bank.account.application.service.BankAccountExternalService {

  private final BankAccountService bankAccountService;

  @Override
  public Mono<?> saveBankAccount(BankAccountDTO bankAccountDTO) {
    return bankAccountService.saveBankAccount(bankAccountDTO);
  }
}
