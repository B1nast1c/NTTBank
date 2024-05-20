package project.transactionsservice.domain.validations.strategies;

import project.transactionsservice.application.validations.TransactionsAppValidations;
import project.transactionsservice.domain.validations.TransactionDomainValidations;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.mapper.GenericMapper;
import project.transactionsservice.infrastructure.serviceCalls.responses.AccountResponse;
import project.transactionsservice.infrastructure.serviceCalls.responses.GenericResponse;
import reactor.core.publisher.Mono;

public class DepositAndWithdrawalStrategy extends TransactionDomainValidations {
  /**
   * Save deposit mono.
   *
   * @param transactionDTO the transaction dto
   * @return the mono
   */
  public Mono<TransactionDTO> saveDeposit(
      TransactionDTO transactionDTO,
      GenericResponse serviceResponse,
      String cliendNumber) {

    AccountResponse accRes = GenericMapper.mapToAny(serviceResponse.getData(), AccountResponse.class);

    return validateTransaction(transactionDTO, serviceResponse)
        .flatMap(dto -> {
          switch (accRes.getType()) {
            case "AHORRO":
              break;
            case "CUENTA_CORRIENTE":
              break;
            case "PLAZO_FIJO":
              break;
            default:
              break;
          }
        });

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
    return TransactionsAppValidations.validateProduct(transaction, serviceResponse)
        .flatMap(TransactionsAppValidations::validateTransactionType);
  }
}
