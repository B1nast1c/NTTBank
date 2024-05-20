package project.infrastructure.adapters.MongoRepo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import project.domain.model.Account.FixedTermAccount;
import reactor.core.publisher.Mono;

public interface FxdTermRepo extends ReactiveMongoRepository<FixedTermAccount, String> {
  Mono<Boolean> existsByClientId(String clientId);
}
