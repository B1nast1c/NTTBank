package project.transactionsservice.infrastructure.strategies;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.transactionsservice.domain.model.Transaction;
import project.transactionsservice.domain.validations.TransactionDomainValidations;
import project.transactionsservice.infrastructure.adapters.mongoRepos.TransactionsRepo;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.exceptions.throwable.InvalidClient;
import project.transactionsservice.infrastructure.servicecalls.responses.AccountResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AccountStrategies {
  private final TransactionsRepo transactionsRepo;

  public AccountStrategies(TransactionsRepo transactionsRepo) {
    this.transactionsRepo = transactionsRepo;
  }

  Mono<TransactionDTO> genericValidation(TransactionDTO transaction, AccountResponse response) {
    if (transaction.getClientNumber().equals(response.getClientDocument())) {
      return Mono.just(transaction);
    }
    log.warn("Client not allowed to do the transaction GENERIC ACCOUNT");
    return Mono.error(new InvalidClient("Client not allowed to do the transaction"));
  }

  public Mono<TransactionDTO> savingsStrategy(TransactionDTO transaction, AccountResponse response) {
    return genericValidation(transaction, response);
  }

  public Mono<TransactionDTO> fxdStrategy(TransactionDTO transaction, AccountResponse response) {
    return genericValidation(transaction, response).
        flatMap(validated -> {
          Flux<Transaction> transactions = transactionsRepo.findAllByProductNumber(transaction.getProductNumber());
          return transactions.collectList()
              .flatMap(list -> TransactionDomainValidations.validateFxdAccount(transaction, list));
        });
  }

  public Mono<TransactionDTO> currAccStrategy(TransactionDTO transaction, AccountResponse response) {
    if (transaction.getClientNumber().equals(response.getClientDocument())
        || response.getAccountTitulars().contains(transaction.getClientNumber())) {
      return Mono.just(transaction);
    }
    log.warn("Client not allowed to do the transaction (CUENTA CORRIENTE)");
    return Mono.error(new InvalidClient("Client not allowed to do the transaction"));
  }
}
