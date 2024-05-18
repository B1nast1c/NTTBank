package project.transactionsservice.infrastructure.adapters.mongoRepos;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import project.transactionsservice.domain.model.Transaction;
import reactor.core.publisher.Flux;

public interface TransactionsRepo extends ReactiveMongoRepository<Transaction, String> {
  Flux<Transaction> findAllByProductNumber(String productNumber);
}
