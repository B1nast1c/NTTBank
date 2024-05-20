package com.nttdata.project.bank.account.infrastructure.dao.repository;

import com.nttdata.project.bank.account.domain.model.BankAccount;
import com.nttdata.project.bank.account.infrastructure.DTO.BankAccountDTO;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BankAccountRepositoryMongo extends ReactiveMongoRepository<BankAccount, String> {

}
