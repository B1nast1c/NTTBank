package project.transactionsservice.application.service;

import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.exceptions.CustomResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TransactionsService {
  Mono<CustomResponse> createTransaction(TransactionDTO transactionDTO);

  Mono<CustomResponse> getTransaction(String transactionId);

  Mono<CustomResponse<List<TransactionDTO>>> getAllTransactions();

  Mono<CustomResponse> getAllTransactionsByProduct(String productNumber);
}
