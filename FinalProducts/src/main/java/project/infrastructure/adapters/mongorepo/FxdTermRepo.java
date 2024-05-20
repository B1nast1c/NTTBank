package project.infrastructure.adapters.mongorepo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import project.domain.model.account.FixedTermAccount;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FxdTermRepo extends ReactiveMongoRepository<FixedTermAccount, String> {
  Mono<Boolean> existsByClientDocument(String clientId);

  Flux<FixedTermAccount> findAllByClientDocument(String clientId);

  Mono<FixedTermAccount> findByAccountNumber(String accountNumber);
}
