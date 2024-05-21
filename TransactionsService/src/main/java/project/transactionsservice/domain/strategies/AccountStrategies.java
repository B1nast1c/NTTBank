package project.transactionsservice.domain.strategies;

import org.springframework.stereotype.Component;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.exceptions.throwable.InvalidClient;
import project.transactionsservice.infrastructure.servicecalls.responses.AccountResponse;
import reactor.core.publisher.Mono;

@Component
public class AccountStrategies {
  public Mono<TransactionDTO> savingsAndFxdStrategy(TransactionDTO transaction, AccountResponse response) {
    if (transaction.getClientNumber().equals(response.getClientDocument())) {
      return Mono.just(transaction);
    }
    return Mono.error(new InvalidClient("Client not allowed to do the transaction"));
  }

  public Mono<TransactionDTO> currAccStrategy(TransactionDTO transaction, AccountResponse response) {
    if (transaction.getClientNumber().equals(response.getClientDocument())
        || response.getAccountTitulars().contains(transaction.getClientNumber())) {
      return Mono.just(transaction);
    }
    return Mono.error(new InvalidClient("Client not allowed to do the transaction"));
  }
}
