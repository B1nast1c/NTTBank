package project.transactionsservice.application.service.domainService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.transactionsservice.application.service.TransactionsService;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.exceptions.CustomResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class TransactionsDomainService implements TransactionsService {
  @Override
  public Mono<CustomResponse<TransactionDTO>> createTransaction(TransactionDTO transactionDTO) {
    return null;
  }

  @Override
  public Mono<CustomResponse<TransactionDTO>> getTransaction(String transactionId) {
    return null;
  }

  @Override
  public Mono<CustomResponse<Flux<TransactionDTO>>> getAllTransactions() {
    return null;
  }

  @Override
  public Mono<CustomResponse<Flux<TransactionDTO>>> getAllTransactionsByProduct(String productNumber) {
    return null;
  }
}
