package project.transactionsservice.domain.validations.strategies;

import project.transactionsservice.domain.validations.TransactionDomainValidations;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import reactor.core.publisher.Mono;

// Hay un caso de una cuenta que solamente puede registrar una transacción por mes, ese elemento se implementa aquí
public class DepositAndWithdrawalStrategy extends TransactionDomainValidations {
  public Mono<TransactionDTO> saveDeposit(TransactionDTO transactionDTO) {
    return null;
  }

  public Mono<TransactionDTO> saveWithdrawal(TransactionDTO transactionDTO) {
    return null;
  }

  @Override
  protected Mono<TransactionDTO> validateTransaction(TransactionDTO transaction) {
    return null;
  }
}
