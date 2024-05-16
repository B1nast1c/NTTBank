package project.infrastructure.adapters.MongoRepo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import project.domain.model.Account.BankAccount;
import project.domain.model.AccountType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface BARepo extends ReactiveMongoRepository<BankAccount, String> {
  Mono<BankAccount> findByAccountNumber(String accountNumber);

  Flux<BankAccount> findAllByClientId(String clientId);

  Mono<Boolean> existsByAccountTypeAndClientId(AccountType accountType, String clientId);
}
