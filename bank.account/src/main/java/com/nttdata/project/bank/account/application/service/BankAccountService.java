package com.nttdata.project.bank.account.application.service;

import com.nttdata.project.bank.account.infrastructure.dto.BankAccountDTO;
import reactor.core.publisher.Mono;

public interface BankAccountService {

  Mono<?> saveBankAccount(BankAccountDTO bankAccountDTO);
}
