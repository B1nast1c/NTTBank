package project.Application.Service;

import project.infrastructure.dto.account.BankAccountDTO;
import project.infrastructure.dto.account.LegalSignerDTO;
import project.infrastructure.dto.account.TitularDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BankAccountService {
  Mono<?> createBankAccount(Object bankAccountDTO);

  Mono<?> getBankAccount(String accountNumber);

  void updateBankAccount(String accountNumber, Mono<BankAccountDTO> bankAccountDTO);

  void deleteBankAccount(String accountNumber);

  Flux<?> getBankAccounts();

  Flux<?> getAllBankAccountsByClientId(String clientId);

  Mono<?> addTitularsToAccount(String accountNumber, Flux<TitularDTO> titularsDTO);

  Mono<?> addLegalSignersToAccount(String accountNumber, Flux<LegalSignerDTO> legalSignersDTO);

  Mono<BankAccountDTO> removeTitularfromAccount(String accountNumber, int titularId);

  Mono<BankAccountDTO> removeLegalSignerfromAccount(String accountNumber, int signerId);
}
