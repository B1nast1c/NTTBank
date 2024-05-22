package project.transactionsservice.domain.strategies.transactions;

import project.transactionsservice.domain.validations.TransactionDomainValidations;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.servicecalls.responses.AccountResponse;
import reactor.core.publisher.Mono;

public class WithdrawalStrategy implements TransactionStrategy {
  @Override
  public Mono<Object> execute(TransactionDTO transaction, AccountResponse accountResponse) {
    // Lógica de retiro
    return TransactionDomainValidations.validateWithdrawal(transaction, accountResponse)
        .map(dto -> {
          // Lógica específica para retiro

          return dto;
        });
  }
}
