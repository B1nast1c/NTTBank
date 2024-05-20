package com.nttdata.project.bank.account.infrastructure.adapters.repository;

import com.nttdata.project.bank.account.domain.model.BankAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BankAccountRepositoryMongo extends ReactiveMongoRepository<BankAccount, String> {

}
