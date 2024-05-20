package com.nttdata.project.bank.account.domain.ports;

import com.nttdata.project.bank.account.infrastructure.dto.BankAccountDTO;
import reactor.core.publisher.Mono;

public interface BankAccountPort {
  //Flux<BankAccountDTO> findAll();
  //Mono<?> findById(String id);
  Mono<?> save(BankAccountDTO bankAccountDTO);
  //Mono<?> update(Long id,Object object);
  //Mono<?> delete(Long id);
}
