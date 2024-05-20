package com.nttdata.project.bank.account.application.service.impl;

import com.nttdata.project.bank.account.application.service.BankAccountService;
import com.nttdata.project.bank.account.domain.ports.BankAccountPort;
import com.nttdata.project.bank.account.infrastructure.dto.BankAccountDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class BankAccountServiceImpl implements BankAccountService {

  private final BankAccountPort bankAccountPort;

  public BankAccountServiceImpl(BankAccountPort bankAccountPort) {
    this.bankAccountPort = bankAccountPort;
  }

  @Override
  public Mono<?> saveBankAccount(BankAccountDTO bankAccountDTO) {
    return bankAccountPort.save(bankAccountDTO);
  }
}
