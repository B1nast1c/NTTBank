package project.transactionsservice.infrastructure.strategies;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.transactionsservice.domain.validations.TransactionDomainValidations;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.exceptions.throwable.InvalidTransaction;
import project.transactionsservice.infrastructure.factory.AccountsFactory;
import project.transactionsservice.infrastructure.helpers.HelperFunctions;
import project.transactionsservice.infrastructure.mapper.GenericMapper;
import project.transactionsservice.infrastructure.servicecalls.AccountService;
import project.transactionsservice.infrastructure.servicecalls.CreditService;
import project.transactionsservice.infrastructure.servicecalls.request.AccountRequest;
import project.transactionsservice.infrastructure.servicecalls.request.CreditRequest;
import project.transactionsservice.infrastructure.servicecalls.responses.AccountResponse;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

/**
 * Estrategias de transacción para realizar acciones específicas según el tipo de transacción.
 */
@Slf4j
@Component
public class TransactionStrategy {
  private final AccountsFactory accountsFactory;
  private final AccountService accountService;
  private final CreditService creditService;
  private final HelperFunctions helpers;
  BiFunction<TransactionDTO, AccountResponse, Mono<TransactionDTO>> strategy;

  /**
   * Constructor de TransactionStrategy.
   *
   * @param accountsFactory fábrica de estrategias de cuentas
   * @param accountService  servicio de cuentas bancarias
   * @param helpers         funciones auxiliares
   */
  public TransactionStrategy(AccountsFactory accountsFactory,
                             AccountService accountService, CreditService creditService,
                             HelperFunctions helpers) {
    this.accountsFactory = accountsFactory;
    this.accountService = accountService;
    this.creditService = creditService;
    this.helpers = helpers;
  }

  /**
   * Guarda una transacción en la cuenta correspondiente.
   *
   * @param transaction la transacción a guardar
   * @param amount      el monto de la transacción
   * @return un Mono que contiene el resultado de la transacción si es exitosa, o un error si falla
   */
  private Mono<Object> saveTransaction(TransactionDTO transaction, double amount) {
    return helpers.getAccountDetails(transaction.getProductNumber()) // Obtener detalles de la cuenta asociada a la transacción
        .flatMap(accountResponse -> {
          strategy = accountsFactory.getStrategy(accountResponse.getAccountType()); // Obtener la estrategia de validación de la cuenta
          return strategy.apply(transaction, accountResponse).flatMap(applied -> // Aplicar la estrategia de validación
              TransactionDomainValidations.validateDeposit(transaction) // Validar la transacción de depósito
                  .flatMap(dto -> {
                    double balance = accountResponse.getBalance();
                    balance += amount; // Actualizar el saldo de la cuenta
                    AccountRequest request = new AccountRequest(balance, accountResponse.getTransactions() + 1); // Crear solicitud de actualización de cuenta
                    return accountService
                        .updateAccount(dto.getProductNumber(), request) // Actualizar la cuenta bancaria
                        .flatMap(updated -> {
                          if (updated.isSuccess()) {
                            Object mapped = GenericMapper.mapToAny(transaction, Object.class); // Mapear la transacción a un objeto genérico
                            return Mono.just(mapped);
                          }
                          return Mono.error(new InvalidTransaction(updated.getData().toString())); // Devolver un error si falla la actualización de la cuenta
                        });
                  }));
        });
  }

  /**
   * Estrategia para realizar un depósito.
   *
   * @param transaction la transacción de depósito
   * @return un Mono que contiene el resultado del depósito si es exitoso, o un error si falla
   */
  public Mono<Object> depositStrategy(TransactionDTO transaction) {
    return saveTransaction(transaction, transaction.getAmmount()); // Llamar al método de guardar transacción para realizar un depósito
  }

  /**
   * Estrategia para realizar un retiro.
   *
   * @param transaction la transacción de retiro
   * @return un Mono que contiene el resultado del retiro si es exitoso, o un error si falla
   */
  public Mono<Object> withdrawalStrategy(TransactionDTO transaction) {
    return saveTransaction(transaction, transaction.getAmmount() * -1); // Llamar al método de guardar transacción para realizar un retiro
  }

  public Mono<Object> creditCardStrategy(TransactionDTO transaction) {
    return helpers.getCreditCardDetails(transaction.getProductNumber())
        .flatMap(cardResponse -> TransactionDomainValidations
            .validateCreditCardCharge(transaction, cardResponse)
            .flatMap(card -> {
              CreditRequest request = new CreditRequest(transaction.getAmmount());
              return creditService.updateCreditCard(transaction.getProductNumber(), request)
                  .flatMap(updated -> {
                    if (updated.isSuccess()) {
                      Object mapped = GenericMapper.mapToAny(transaction, Object.class); // Mapear la transacción a un objeto genérico
                      return Mono.just(mapped);
                    }
                    return Mono.error(new InvalidTransaction(updated.getData().toString())); // Devolver un error si falla la actualización de la cuenta
                  });
            })
        );
  }

  public Mono<Object> creditStrategy(TransactionDTO transaction) {
    return helpers.getCreditDetails(transaction.getProductNumber())
        .flatMap(creditResponse -> TransactionDomainValidations
            .validateCreditPayment(transaction)
            .flatMap(credit -> {
              CreditRequest request = new CreditRequest(false, transaction.getAmmount());
              if (transaction.getAmmount() >= creditResponse.getLimit()) {
                request.setPaid(true);
              }
              return creditService.updateCredit(transaction.getProductNumber(), request)
                  .flatMap(updated -> {
                    if (updated.isSuccess()) {
                      Object mapped = GenericMapper.mapToAny(transaction, Object.class); // Mapear la transacción a un objeto genérico
                      return Mono.just(mapped);
                    }
                    return Mono.error(new InvalidTransaction(updated.getData().toString())); // Devolver un error si falla la actualización de la cuenta
                  });
            })
        );
  }


}