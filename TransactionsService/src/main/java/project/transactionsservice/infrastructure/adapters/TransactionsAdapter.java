package project.transactionsservice.infrastructure.adapters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import project.transactionsservice.application.validations.TransactionsAppValidations;
import project.transactionsservice.domain.ports.TransactionsPort;
import project.transactionsservice.domain.validations.TransactionDomainValidations;
import project.transactionsservice.infrastructure.adapters.mongoRepos.TransactionsRepo;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.exceptions.CustomError;
import project.transactionsservice.infrastructure.exceptions.throwable.NotFoundTransaction;
import project.transactionsservice.infrastructure.mapper.GenericMapper;
import project.transactionsservice.infrastructure.serviceCalls.AccountService;
import project.transactionsservice.infrastructure.serviceCalls.CreditService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class TransactionsAdapter implements TransactionsPort {
  private final TransactionsRepo transactionsRepo;
  private final AccountService accountService;
  private final CreditService creditService;

  public TransactionsAdapter(TransactionsRepo transactionsRepo, AccountService accountService, CreditService creditService) {
    this.transactionsRepo = transactionsRepo;
    this.accountService = accountService;
    this.creditService = creditService;
  }

  /*private Mono<TransactionDTO> validateAndInsertTransaction(TransactionDTO transaction) {
    return TransactionsAppValidations.validateTransactionType(transaction)
        .flatMap(first -> {

        })
        .validateClientType(client)
        .flatMap(item -> appValidations.validateDocumentNumber(client))
        .map(GenericMapper::mapToEntity)
        .flatMap(clientRepository::insert);
  }*/

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
        .map(GenericMapper::mapToDto)
        .switchIfEmpty(
            Flux.error(new NotFoundTransaction("The bank product has no transactions"))
        );
  }
}
