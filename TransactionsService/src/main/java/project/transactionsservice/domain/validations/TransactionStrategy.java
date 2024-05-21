package project.transactionsservice.domain.validations;

import project.transactionsservice.infrastructure.dto.TransactionDTO;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface TransactionStrategy {
  Mono<TransactionDTO> validateTransaction(TransactionDTO transactionDTO);
}
