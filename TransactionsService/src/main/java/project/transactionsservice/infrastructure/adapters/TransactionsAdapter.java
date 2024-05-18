package project.transactionsservice.infrastructure.adapters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import project.transactionsservice.domain.ports.TransactionsPort;
import project.transactionsservice.infrastructure.adapters.mongoRepos.TransactionsRepo;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.exceptions.CustomError;
import project.transactionsservice.infrastructure.exceptions.throwable.NotFoundTransaction;
import project.transactionsservice.infrastructure.mapper.GenericMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class TransactionsAdapter implements TransactionsPort {
  private final TransactionsRepo transactionsRepo;

  public TransactionsAdapter(TransactionsRepo transactionsRepo) {
    this.transactionsRepo = transactionsRepo;
  }

  @Override
  public Mono<TransactionDTO> saveTransaction(TransactionDTO transactionDTO) {
    return null;
  }

  @Override
  public Mono<TransactionDTO> getTransaction(String transactionNumber) {
    return transactionsRepo.findById(transactionNumber)
        .flatMap(transaction -> {
          log.info("Transaction found -> {}", transaction.getTransactionId());
          return Mono.just(GenericMapper.mapToDto(transaction));
        }).switchIfEmpty(
            Mono.defer(() -> {
              log.warn("Transaction not found -> {}", CustomError.ErrorType.TRANSACTION_NOT_FOUND);
              return Mono.error(new NotFoundTransaction("The transaction you are looking for does not exist"));
            }));
  }

  @Override
  public Flux<TransactionDTO> getAllTransactions() {
    return transactionsRepo.findAll()
        .map(GenericMapper::mapToDto);
  }

  @Override
  public Flux<TransactionDTO> getTransactionsByProductNumber(String productNumber) { // Adicional, lanzar una excepcion en caso de que la cuenta no exista
    return transactionsRepo.findAllByProductNumber(productNumber)
        .map(GenericMapper::mapToDto);
  }
}
