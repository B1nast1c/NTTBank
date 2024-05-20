package project.infrastructure.adapters.MongoRepo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import project.domain.model.Account.CurrentAccount;
import reactor.core.publisher.Mono;

public interface CurrAccRepo extends ReactiveMongoRepository<CurrentAccount, String> {
  Mono<Boolean> existsByClientId(String clientId);
}
