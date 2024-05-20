package com.nttdata.project.bank.account.infrastructure.repository.impl;

import com.nttdata.project.bank.account.domain.model.BankAccount;
import com.nttdata.project.bank.account.domain.repository.BankAccountRepository;
import com.nttdata.project.bank.account.infrastructure.DTO.BankAccountDTO;
import com.nttdata.project.bank.account.infrastructure.dao.repository.BankAccountRepositoryMongo;
import com.nttdata.project.bank.account.infrastructure.Mapper.BankAccountMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class BankAccountRepositoryImpl implements BankAccountRepository {

    private final BankAccountRepositoryMongo bankAccountRepositoryMongo;

    @Override
    public Mono<BankAccount> save(BankAccountDTO bankAccountDTO) {
        //return bankAccountRepositoryMongo.save(bankAccount)
        //        .map(bankAccountMapper::mapToDTO);
        BankAccount bankAccount = BankAccountMapper.mapToEntity(bankAccountDTO);
        return bankAccountRepositoryMongo.save(bankAccount);
        //return Mono.just(bankAccount).map(bankAccountMapper::mapToDTO).flatMap(bankAccountRepositoryMongo::insert);
    }
}
