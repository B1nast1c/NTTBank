package project.transactionsservice.domain.validations;

import project.transactionsservice.infrastructure.dto.TransactionDTO;
import reactor.core.publisher.Mono;

// NOTA -> Cada validación del tipo de cuenta corresponde a una estrategia
// Validamos que el tipo de cuenta corresponda a la transacción seleccionada
// Cada transacción comparte su guardado, pero las validaciones son diferentes

public abstract class TransactionDomainValidations {
  static Mono<TransactionDTO> validateAmmount(TransactionDTO transactionDTO) {
    return null;
  }

  protected abstract Mono<TransactionDTO> validateTransaction(TransactionDTO transaction);
}
