package project.transactionsservice.infrastructure.strategies.transactions;

import project.transactionsservice.infrastructure.dto.TransactionDTO;
import reactor.core.publisher.Mono;

public interface TransactionStrategy {
  Mono<Object> execute(TransactionDTO transaction);
}