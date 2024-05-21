package project.transactionsservice.domain.ports;

import project.transactionsservice.infrastructure.dto.TransactionDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionsPort {
  Mono<Object> saveTransaction(TransactionDTO transactionDTO);

  Mono<TransactionDTO> getTransaction(String transactionNumber);

  Flux<TransactionDTO> getAllTransactions();

  Flux<TransactionDTO> getTransactionsByProductNumber(String productNumber);
}
