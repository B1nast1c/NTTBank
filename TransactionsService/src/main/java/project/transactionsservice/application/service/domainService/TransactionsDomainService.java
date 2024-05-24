package project.transactionsservice.application.service.domainService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.transactionsservice.application.service.TransactionsService;
import project.transactionsservice.domain.ports.TransactionsPort;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.exceptions.CustomError;
import project.transactionsservice.infrastructure.exceptions.CustomResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Servicio de dominio para manejar las transacciones.
 */
@Slf4j
@Service
public class TransactionsDomainService implements TransactionsService {
  private final TransactionsPort transactionsPort;

  /**
   * Crea una nueva instancia del servicio de dominio de transacciones.
   *
   * @param transactionsPort el puerto de transacciones
   */
  public TransactionsDomainService(TransactionsPort transactionsPort) {
    this.transactionsPort = transactionsPort;
  }

  /**
   * Crea una nueva transacción.
   *
   * @param transactionDTO la transacción a crear
   * @return una respuesta indicando el resultado de la creación
   */
  @Override
  public Mono<CustomResponse<Object>> createTransaction(TransactionDTO transactionDTO) {
    return transactionsPort.saveTransaction(transactionDTO)
        .flatMap(transaction -> {
              log.info("Account created successfully");
              CustomResponse<Object> result = new CustomResponse<>(true, transaction);
              return Mono.just(result);
            }
        )
        .onErrorResume(throwable -> {
          CustomError error = new CustomError(throwable.getMessage(), CustomError.ErrorType.POSTING_ERROR);
          log.warn(throwable.getMessage());
          CustomResponse<Object> badResponse = new CustomResponse<>(false, error);
          return Mono.just(badResponse);
        });
  }

  /**
   * Obtiene una transacción por su ID.
   *
   * @param transactionId el ID de la transacción
   * @return una respuesta con la transacción encontrada
   */
  @Override
  public Mono<CustomResponse<Object>> getTransaction(String transactionId) {
    return transactionsPort.getTransaction(transactionId)
        .flatMap(transaction -> {
          log.info("Transaction found successfully");
          CustomResponse<Object> result = new CustomResponse<>(true, transaction);
          return Mono.just(result);
        })
        .onErrorResume(throwable -> {
          CustomError error = new CustomError(throwable.getMessage(), CustomError.ErrorType.GETTING_ERROR);
          log.warn(throwable.getMessage());
          CustomResponse<Object> badResponse = new CustomResponse<>(false, error);
          return Mono.just(badResponse);
        });
  }

  /**
   * Obtiene todas las transacciones.
   *
   * @return una respuesta con todas las transacciones
   */
  @Override
  public Mono<CustomResponse<List<TransactionDTO>>> getAllTransactions() {
    Flux<TransactionDTO> transactions = transactionsPort.getAllTransactions();
    Mono<List<TransactionDTO>> listMono = transactions.collectList();
    return listMono.map(list -> new CustomResponse<>(true, list));
  }

  /**
   * Obtiene transacciones por el número del producto bancario.
   *
   * @param productNumber el número del producto bancario
   * @return una respuesta con las transacciones relacionadas al producto
   */
  @Override
  public Mono<CustomResponse<Object>> getAllTransactionsByProduct(String productNumber) {
    return transactionsPort.getTransactionsByProductNumber(productNumber)
        .collectList()
        .map(transactions -> new CustomResponse<Object>(true, transactions))
        .onErrorResume(throwable -> {
          log.warn(throwable.getMessage());
          CustomError error = new CustomError(throwable.getMessage(), CustomError.ErrorType.GETTING_ERROR);
          CustomResponse<Object> badResponse = new CustomResponse<>(false, error);
          return Mono.just(badResponse);
        });
  }
}