package project.transactionsservice.domain.validations.strategies;

import project.transactionsservice.domain.validations.TransactionDomainValidations;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.serviceCalls.responses.GenericResponse;
import reactor.core.publisher.Mono;

public class CreditPaymentStrategy extends TransactionDomainValidations {
  public Mono<TransactionDTO> saveCreditPayment(TransactionDTO transactionDTO) {
    return null;
  }

  @Override
  protected Mono<TransactionDTO> validateTransaction(TransactionDTO transaction, GenericResponse serviceResponse) {
    return null;
  }
}
