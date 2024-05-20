package project.infrastructure.adapters.MongoRepo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import project.domain.model.Credits.CreditCard;

public interface CreditCardRepo extends ReactiveMongoRepository<CreditCard, String> {
}
