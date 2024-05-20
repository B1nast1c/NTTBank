package com.nttdata.project.bank.account.domain.ports;

import com.nttdata.project.bank.account.infrastructure.DTO.BankAccountDTO;
import reactor.core.publisher.Mono;

public interface BankAccountRepository {
  //Flux<BankAccountDTO> findAll();
  //Mono<?> findById(String id);
  Mono<?> save(BankAccountDTO bankAccountDTO);
  //Mono<?> update(Long id,Object object);
  //Mono<?> delete(Long id);
}
