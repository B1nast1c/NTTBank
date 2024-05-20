package com.nttdata.project.bank.account.domain.service;

import com.nttdata.project.bank.account.domain.model.BankAccount;
import com.nttdata.project.bank.account.infrastructure.DTO.BankAccountDTO;
import reactor.core.publisher.Mono;

public interface BankAccountService {

    Mono<?> saveBankAccount(BankAccountDTO bankAccountDTO);
}
