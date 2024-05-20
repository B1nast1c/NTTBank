package project.infrastructure.adapters.mongorepo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import project.domain.model.account.CurrentAccount;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CurrAccRepo extends ReactiveMongoRepository<CurrentAccount, String> {
  Mono<Boolean> existsByClientDocument(String clientId);

  Flux<CurrentAccount> findAllByClientDocument(String clientId);

  Mono<CurrentAccount> findByAccountNumber(String accountNumber);
}
