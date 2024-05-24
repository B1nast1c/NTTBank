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

/**
 * Estrategias de cuenta para realizar validaciones específicas antes de procesar una transacción.
 */
@Slf4j
@Component
public class AccountStrategies {
  private final TransactionsRepo transactionsRepo;

  /**
   * Constructor de AccountStrategies.
   *
   * @param transactionsRepo repositorio para acceder a las transacciones
   */
  public AccountStrategies(TransactionsRepo transactionsRepo) {
    this.transactionsRepo = transactionsRepo;
  }

  /**
   * Estrategia de validación genérica para todas las cuentas.
   *
   * @param transaction la transacción a validar
   * @param response    la respuesta de la cuenta
   * @return un Mono que contiene la transacción validada si es exitosa, o un error si falla la validación
   */
  Mono<TransactionDTO> genericValidation(TransactionDTO transaction, AccountResponse response) {
    if (transaction.getClientNumber().equals(response.getClientDocument())) { // Comprueba si el número de cliente coincide con el documento del cliente
      return Mono.just(transaction); // Retorna la transacción si el cliente es válido
    }
    log.warn("Unauthorized client trying to perform transaction on account"); // Registra un mensaje de advertencia si el cliente no está autorizado
    return Mono.error(new InvalidClient("The client is not authorized to perform the transaction")); // Retorna un error si el cliente no está autorizado
  }

  /**
   * Estrategia de validación para cuentas de ahorro.
   *
   * @param transaction la transacción a validar
   * @param response    la respuesta de la cuenta
   * @return un Mono que contiene la transacción validada si es exitosa, o un error si falla la validación
   */
  public Mono<TransactionDTO> savingsStrategy(TransactionDTO transaction, AccountResponse response) {
    return genericValidation(transaction, response); // Utiliza la estrategia de validación genérica para todas las cuentas
  }

  /**
   * Estrategia de validación para cuentas a plazo fijo.
   *
   * @param transaction la transacción a validar
   * @param response    la respuesta de la cuenta
   * @return un Mono que contiene la transacción validada si es exitosa, o un error si falla la validación
   */
  public Mono<TransactionDTO> fxdStrategy(TransactionDTO transaction, AccountResponse response) {
    return genericValidation(transaction, response) // Utiliza la estrategia de validación genérica para todas las cuentas
        .flatMap(validated -> {
          Flux<Transaction> transactions = transactionsRepo.findAllByProductNumber(transaction.getProductNumber()); // Obtiene todas las transacciones asociadas al producto
          return transactions.collectList() // Recolecta todas las transacciones en una lista
              .flatMap(list -> TransactionDomainValidations.validateFxdAccount(transaction, list)); // Realiza validaciones adicionales para cuentas a plazo fijo
        });
  }

  /**
   * Estrategia de validación para cuentas corrientes.
   *
   * @param transaction la transacción a validar
   * @param response    la respuesta de la cuenta
   * @return un Mono que contiene la transacción validada si es exitosa, o un error si falla la validación
   */
  public Mono<TransactionDTO> currAccStrategy(TransactionDTO transaction, AccountResponse response) {
    if (transaction.getClientNumber().equals(response.getClientDocument()) // Comprueba si el número de cliente coincide con el documento del cliente
        || response.getAccountTitulars().contains(transaction.getClientNumber())) { // Comprueba si el cliente es titular de la cuenta
      return Mono.just(transaction); // Retorna la transacción si el cliente es válido
    }
    log.warn("Unauthorized client trying to perform transaction on current account"); // Registra un mensaje de advertencia si el cliente no está autorizado
    return Mono.error(new InvalidClient("The client is not authorized to perform the transaction")); // Retorna un error si el cliente no está autorizado
  }
}