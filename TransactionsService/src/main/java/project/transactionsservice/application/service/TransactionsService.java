package project.transactionsservice.application.service;

import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.exceptions.CustomResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TransactionsService {
  Mono<CustomResponse<Object>> createTransaction(TransactionDTO transactionDTO);

  Mono<CustomResponse<Object>> getTransaction(String transactionId);

  Mono<CustomResponse<List<TransactionDTO>>> getAllTransactions();

  Mono<CustomResponse<Object>> getAllTransactionsByProduct(String productNumber);
}
