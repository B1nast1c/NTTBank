package com.nttdata.project.bank.account.infrastructure.adapters;

import com.nttdata.project.bank.account.domain.model.BankAccount;
import com.nttdata.project.bank.account.domain.ports.BankAccountPort;
import com.nttdata.project.bank.account.infrastructure.adapters.repository.BankAccountRepositoryMongo;
import com.nttdata.project.bank.account.infrastructure.dto.BankAccountDTO;
import com.nttdata.project.bank.account.infrastructure.mapper.GenericMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

// IMPLEMENTACION DEL PUERTO, DEL DOMINIO (OSEA EL REPOSITORIO)

@Repository
public class BankAccountAdapter implements BankAccountPort {
  private final BankAccountRepositoryMongo accountRepository;

  public BankAccountAdapter(BankAccountRepositoryMongo accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override
  public Mono<BankAccount> save(BankAccountDTO bankAccountDTO) {
    //return bankAccountRepositoryMongo.save(bankAccount)
    //        .map(bankAccountMapper::mapToDTO);
    BankAccount bankAccount = GenericMapper.mapToEntity(bankAccountDTO);
    return accountRepository.save(bankAccount);
    //return Mono.just(bankAccount).map(bankAccountMapper::mapToDTO).flatMap(bankAccountRepositoryMongo::insert);
  }
}
