package com.nttdata.project.bank.account.application.service;

import com.nttdata.project.bank.account.infrastructure.dto.BankAccountDTO;
import reactor.core.publisher.Mono;

public interface BankAccountExternalService {
  //Flux<BankAccount> getAllAccounts();
  //Mono<?> getAccount(String id);
  //void addAccount(BankAccountDTO bankAccount);
  //Mono<?> updateAccount(String id, BankAccountDTO bankAccount);
  //Mono<?> deleteAccount(String id);
  Mono<?> saveBankAccount(BankAccountDTO bankAccountDTO);
}
