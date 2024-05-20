package com.nttdata.project.bank.account.domain.service.impl;

import com.nttdata.project.bank.account.domain.ports.BankAccountRepository;
import com.nttdata.project.bank.account.domain.service.BankAccountService;
import com.nttdata.project.bank.account.infrastructure.DTO.BankAccountDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

  private final BankAccountRepository bankAccountRepository;

  @Override
  public Mono<?> saveBankAccount(BankAccountDTO bankAccountDTO) {
    return bankAccountRepository.save(bankAccountDTO);
  }
}
