package project.transactionsservice.domain.validations.strategies;

import project.transactionsservice.domain.validations.TransactionDomainValidations;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.serviceCalls.responses.AccountResponse;
import project.transactionsservice.infrastructure.serviceCalls.responses.GenericResponse;
import reactor.core.publisher.Mono;

/**
 * The type Deposit and withdrawal strategy.
 */

// Hay un caso de una cuenta que solamente puede registrar una transacción por mes, ese elemento se implementa aquí
public class DepositAndWithdrawalStrategy extends TransactionDomainValidations {
  /**
   * Save deposit mono.
   *
   * @param transactionDTO the transaction dto
   * @return the mono
   */
  public Mono<TransactionDTO> saveDeposit(
      TransactionDTO transactionDTO,
      AccountResponse accountResponse,
      String cliendNumber) {

    // LÓGICA POR CADA TIPO DE CUENTA (LIMITE DE TRANSACCION, FECHA DE TRANSACCION)

    return null;
  }

  /**
   * Save withdrawal mono.
   *
   * @param transactionDTO the transaction dto
   * @return the mono
   */
  public Mono<TransactionDTO> saveWithdrawal(
      TransactionDTO transactionDTO,
      AccountResponse accountResponse,
      String cliendNumber) {

    // LÓGICA POR CADA TIPO DE CUENTA (LIMITE DE TRANSACCION, FECHA DE TRANSACCION)

    return null;
  }

  @Override
  protected Mono<TransactionDTO> validateTransaction(
      TransactionDTO transaction,
      GenericResponse serviceResponse) {
    return TransactionDomainValidations.validateProduct(transaction, serviceResponse);
  }
}
