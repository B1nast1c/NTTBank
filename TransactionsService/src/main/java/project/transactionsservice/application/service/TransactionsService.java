package project.transactionsservice.application.service;

import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.exceptions.CustomResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionsService {
  Mono<CustomResponse<TransactionDTO>> createTransaction(TransactionDTO transactionDTO);

  Mono<CustomResponse<TransactionDTO>> getTransaction(String transactionId);

  Mono<CustomResponse<Flux<TransactionDTO>>> getAllTransactions();

  Mono<CustomResponse<Flux<TransactionDTO>>> getAllTransactionsByProduct(String productNumber);
}
