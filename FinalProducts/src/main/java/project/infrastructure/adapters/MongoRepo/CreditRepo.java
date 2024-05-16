package project.infrastructure.adapters.MongoRepo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import project.domain.model.Credits.Credit;

public interface CreditRepo extends ReactiveMongoRepository<Credit, String> {
}
