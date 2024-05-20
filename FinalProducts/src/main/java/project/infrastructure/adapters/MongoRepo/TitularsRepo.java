package project.infrastructure.adapters.MongoRepo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import project.domain.model.Account.Titular;

public interface TitularsRepo extends ReactiveMongoRepository<Titular, Integer> {
}
