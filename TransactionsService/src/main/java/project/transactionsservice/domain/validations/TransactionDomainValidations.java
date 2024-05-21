package project.transactionsservice.domain.validations;

import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.exceptions.throwable.InvalidAmmount;
import project.transactionsservice.infrastructure.mapper.GenericMapper;
import project.transactionsservice.infrastructure.servicecalls.responses.AccountResponse;
import project.transactionsservice.infrastructure.servicecalls.responses.CreditResponse;
import project.transactionsservice.infrastructure.servicecalls.responses.GenericResponse;
import reactor.core.publisher.Mono;

// NOTA -> Cada validación del tipo de cuenta corresponde a una estrategia
// Validamos que el tipo de cuenta corresponda a la transacción seleccionada
// Cada transacción comparte su guardado, pero las validaciones son diferentes

public abstract class TransactionDomainValidations {
  public static Mono<TransactionDTO> validateDeposit(TransactionDTO transaction) {
    if (transaction.getAmmount() <= 100000) {
      return Mono.just(transaction);
    }
    return Mono.error(new InvalidAmmount("The deposit ammount is too big"));
  }

  public static Mono<TransactionDTO> validateWithdrawal(TransactionDTO transaction, AccountResponse account) {
    if (account.getBalance() >= transaction.getAmmount()) {
      return Mono.just(transaction);
    }
    return Mono.error(new InvalidAmmount("Insufficient funds for withdrawal"));
  }

  public static Mono<TransactionDTO> validateCreditPayment(TransactionDTO transaction, CreditResponse credit) {
    if (credit.getAmmount() == transaction.getAmmount()) {
      return Mono.just(transaction);
    } else {
      return Mono.error(new InvalidAmmount("The credit payment ammount does not match the required ammount"));
    }
  }

  public static Mono<TransactionDTO> validateCreditCardCharge(TransactionDTO transaction, CreditResponse creditCard) {
      if (creditCard.getAmmount() >= transaction.getAmmount()) {
        TransactionDTO mappedTransaction = GenericMapper.mapToDto(creditCard);
        return Mono.just(mappedTransaction);
      }
        return Mono.error(new InvalidAmmount("The charge exceeds the available credit limit"));
    }

  protected abstract Mono<TransactionDTO> validateTransaction(TransactionDTO transaction, GenericResponse serviceResponse);
}
