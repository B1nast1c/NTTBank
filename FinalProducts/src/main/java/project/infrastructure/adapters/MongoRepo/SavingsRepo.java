package project.infrastructure.adapters.MongoRepo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import project.domain.model.Account.SavingsAccount;
import reactor.core.publisher.Mono;

public interface SavingsRepo extends ReactiveMongoRepository<SavingsAccount, String> {
  Mono<Boolean> existsByClientId(String clientId);
}
