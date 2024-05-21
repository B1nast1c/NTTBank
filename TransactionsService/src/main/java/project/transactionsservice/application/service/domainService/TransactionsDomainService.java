package project.transactionsservice.application.service.domainService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.transactionsservice.application.controller.TransactionsController;
import project.transactionsservice.application.service.TransactionsService;
import project.transactionsservice.domain.ports.TransactionsPort;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.exceptions.CustomError;
import project.transactionsservice.infrastructure.exceptions.CustomResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class TransactionsDomainService implements TransactionsService {
  private final TransactionsPort transactionsPort;

  public TransactionsDomainService(TransactionsPort transactionsPort) {
    this.transactionsPort = transactionsPort;
  }

  @Override
  public Mono<CustomResponse> createTransaction(TransactionDTO transactionDTO) {
    return transactionsPort.saveTransaction(transactionDTO)
        .flatMap(
            transaction -> {
              CustomResponse result = new CustomResponse<>(true, transaction);
              return Mono.just(result);
            }
        );
        /*.onErrorResume(throwable -> {
          CustomError error = new CustomError(throwable.getMessage(), CustomError.ErrorType.POSTING_ERROR);
          CustomResponse<CustomError> badResponse = new CustomResponse<>(false, error);
          return Mono.just(badResponse).cast(CustomResponse.class);
        });*/
  }

  @Override
  public Mono<CustomResponse> getTransaction(String transactionId) {
    return transactionsPort.getTransaction(transactionId)
        .flatMap(transaction -> {
          CustomResponse result = new CustomResponse<>(true, transaction);
          return Mono.just(result);
        })
        .onErrorResume(throwable -> {
          CustomError error = new CustomError(throwable.getMessage(), CustomError.ErrorType.GETTING_ERROR);
          CustomResponse<CustomError> badResponse = new CustomResponse<>(false, error);
          return Mono.just(badResponse).cast(CustomResponse.class);
        });
  }

  @Override
  public Mono<CustomResponse<List<TransactionDTO>>> getAllTransactions() {
    Flux<TransactionDTO> transactions = transactionsPort.getAllTransactions();
    Mono<List<TransactionDTO>> listMono = transactions.collectList();
    return listMono.map(list -> new CustomResponse<>(true, list));
  }

  @Override
  public Mono<CustomResponse> getAllTransactionsByProduct(String productNumber) {
    return transactionsPort.getTransactionsByProductNumber(productNumber)
        .collectList()
        .map(transactions -> new CustomResponse<>(true, transactions))
        .cast(CustomResponse.class) // Casteo correspondiente a una respuesta correcta
        .onErrorResume(throwable -> {
          CustomError error = new CustomError(throwable.getMessage(), CustomError.ErrorType.GETTING_ERROR);
          CustomResponse<CustomError> badResponse = new CustomResponse<>(false, error);
          return Mono.just(badResponse).cast(CustomResponse.class);
        });
  }
}
