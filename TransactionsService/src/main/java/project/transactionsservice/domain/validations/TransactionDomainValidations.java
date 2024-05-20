package project.transactionsservice.domain.validations;

import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.exceptions.throwable.InvalidAmmount;
import project.transactionsservice.infrastructure.exceptions.throwable.InvalidClient;
import project.transactionsservice.infrastructure.exceptions.throwable.NotFoundProduct;
import project.transactionsservice.infrastructure.mapper.GenericMapper;
import project.transactionsservice.infrastructure.serviceCalls.responses.AccountResponse;
import project.transactionsservice.infrastructure.serviceCalls.responses.CreditResponse;
import project.transactionsservice.infrastructure.serviceCalls.responses.GenericResponse;
import reactor.core.publisher.Mono;

// NOTA -> Cada validación del tipo de cuenta corresponde a una estrategia
// Validamos que el tipo de cuenta corresponda a la transacción seleccionada
// Cada transacción comparte su guardado, pero las validaciones son diferentes

public abstract class TransactionDomainValidations {
  public static Mono<TransactionDTO> validateDeposit(TransactionDTO transaction) {
    if (transaction.getAmount() <= 100000) {
      return Mono.just(transaction);
    }
    return Mono.error(new InvalidAmmount("The deposit ammount is too big"));
  }

  public static Mono<TransactionDTO> validateWithdrawal(TransactionDTO transaction, AccountResponse account) {
    if (account.getBalance() >= transaction.getAmount()) {
      return Mono.just(transaction);
    }
    return Mono.error(new InvalidAmmount("Insufficient funds for withdrawal"));
  }

  public static Mono<TransactionDTO> validateCreditPayment(TransactionDTO transaction, CreditResponse credit) {
    if (credit.getAmmount() == transaction.getAmount()) {
      return Mono.just(transaction);
    } else {
      return Mono.error(new InvalidAmmount("The credit payment amount does not match the required amount"));
    }
  }

  public static Mono<TransactionDTO> validateCreditCardCharge(TransactionDTO transaction, CreditResponse creditCard) {
      if (creditCard.getAmmount() >= transaction.getAmount()) {
        TransactionDTO mappedTransaction = GenericMapper.mapToDto(creditCard);
        return Mono.just(mappedTransaction);
      }
        return Mono.error(new InvalidAmmount("The charge exceeds the available credit limit"));
    }

  /**
   * Validación que verifica que ya sea el cliente o titulares de cuenta, depende del tipo de cuenta
   * son los unicos autorizados en realizar transacciones de depósito o retiro. (APLICA SOLAMENTE A CUENTAS BANCARIAS)
   *
   * @param transactionDTO La transacción a insertar y el cliente que la hace.
   * @param clientCode La transacción a insertar y el cliente que la hace.
   * @return La transacción dado el caso que todas las validaciones sean correctas, un error si no se
   * cumplen las condiciones establecidas.
   */
  public static Mono<TransactionDTO> validateAllowedClient(TransactionDTO transactionDTO, String clientCode, AccountResponse account) {
    if (clientCode == account.getClientNumber()) {
      return Mono.just(transactionDTO);
    }
    return Mono.error(new InvalidClient("The client is not an account owner or titular"));
  }

  protected abstract Mono<TransactionDTO> validateTransaction(TransactionDTO transaction, GenericResponse serviceResponse);
}
