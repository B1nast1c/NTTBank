package project.transactionsservice.infrastructure.adapters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import project.transactionsservice.application.validations.TransactionsAppValidations;
import project.transactionsservice.domain.model.Transaction;
import project.transactionsservice.domain.ports.TransactionsPort;
import project.transactionsservice.infrastructure.adapters.mongoRepos.TransactionsRepo;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.exceptions.CustomError;
import project.transactionsservice.infrastructure.exceptions.throwable.NotFoundTransaction;
import project.transactionsservice.infrastructure.factory.TransactionStrategyFactory;
import project.transactionsservice.infrastructure.mapper.GenericMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * Adaptador que implementa la interfaz TransactionsPort y maneja la lógica de acceso a datos para las transacciones.
 */
@Slf4j
@Repository
public class TransactionsAdapter implements TransactionsPort {
  private final TransactionStrategyFactory transactionsFactory;
  private final TransactionsRepo transactionsRepo;

  /**
   * Constructor de TransactionsAdapter.
   *
   * @param transactionsFactory fábrica de estrategias de transacciones
   * @param transactionsRepo    repositorio de transacciones
   */
  public TransactionsAdapter(TransactionStrategyFactory transactionsFactory, TransactionsRepo transactionsRepo) {
    this.transactionsFactory = transactionsFactory;
    this.transactionsRepo = transactionsRepo;
  }

  /**
   * Guarda una transacción después de validarla y aplicar la estrategia correspondiente.
   *
   * @param transactionDTO el objeto DTO de la transacción a guardar
   * @return un Mono que emite el objeto de la transacción guardada
   */
  public Mono<Object> saveTransaction(TransactionDTO transactionDTO) {
    String transactionType = transactionDTO.getTransactionType();
    return TransactionsAppValidations.validateTransactionType(transactionDTO)
        .flatMap(validatedTransaction -> {
          Function<TransactionDTO, Mono<Object>> strategy = transactionsFactory.getStrategy(transactionType);
          Mono<Object> createdTransaction = strategy.apply(validatedTransaction);
          return createdTransaction
              .flatMap(inserting -> {
                Transaction transaction = GenericMapper.mapToAny(inserting, Transaction.class);
                return transactionsRepo.save(transaction)
                    .map(inserted -> GenericMapper.mapToAny(inserted, TransactionDTO.class));
              });
        });
  }

  /**
   * Obtiene una transacción por su número de transacción.
   *
   * @param transactionNumber el número de la transacción a buscar
   * @return un Mono que emite el objeto DTO de la transacción encontrada, o un error si no se encuentra
   */
  @Override
  public Mono<TransactionDTO> getTransaction(String transactionNumber) {
    return transactionsRepo.findById(transactionNumber)
        .flatMap(transaction -> {
          log.info("Found transaction -> {}", transaction.getTransactionId());
          return Mono.just(GenericMapper.mapToDto(transaction));
        }).switchIfEmpty(
            Mono.defer(() -> {
              log.warn("Not found transaction -> {}", CustomError.ErrorType.TRANSACTION_NOT_FOUND);
              return Mono.error(new NotFoundTransaction("The transaction was not found"));
            }));
  }

  /**
   * Obtiene todas las transacciones.
   *
   * @return un Flux que emite una lista de objetos DTO de todas las transacciones
   */
  @Override
  public Flux<TransactionDTO> getAllTransactions() {
    return transactionsRepo.findAll()
        .map(GenericMapper::mapToDto);
  }

  /**
   * Obtiene todas las transacciones asociadas a un número de producto.
   *
   * @param productNumber el número del producto bancario
   * @return un Flux que emite una lista de objetos DTO de las transacciones asociadas al producto bancario, o un error si no hay transacciones
   */
  @Override
  public Flux<TransactionDTO> getTransactionsByProductNumber(String productNumber) {
    return transactionsRepo.findAllByProductNumber(productNumber)
        .map(GenericMapper::mapToDto)
        .switchIfEmpty(
            Flux.error(new NotFoundTransaction("The transaction was not found and has no transactions"))
        );
  }
}