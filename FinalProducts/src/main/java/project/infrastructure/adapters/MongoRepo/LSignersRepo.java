package project.infrastructure.adapters.MongoRepo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import project.domain.model.Account.LegalSigner;

public interface LSignersRepo extends ReactiveMongoRepository<LegalSigner, Integer> {
}
