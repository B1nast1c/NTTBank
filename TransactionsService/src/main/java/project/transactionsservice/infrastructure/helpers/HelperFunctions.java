package project.transactionsservice.infrastructure.helpers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.transactionsservice.infrastructure.exceptions.throwable.NotFoundProduct;
import project.transactionsservice.infrastructure.mapper.GenericMapper;
import project.transactionsservice.infrastructure.servicecalls.AccountService;
import project.transactionsservice.infrastructure.servicecalls.CreditService;
import project.transactionsservice.infrastructure.servicecalls.responses.AccountResponse;
import project.transactionsservice.infrastructure.servicecalls.responses.CreditResponse;
import reactor.core.publisher.Mono;

/**
 * Clase HelperFunctions que contiene funciones auxiliares para el servicio de cuentas.
 */
@Slf4j
@Component
public class HelperFunctions {
  private final AccountService accountService;
  private final CreditService creditService;

  /**
   * Constructor de HelperFunctions.
   *
   * @param accountService servicio de cuentas para obtener detalles de la cuenta
   */
  public HelperFunctions(AccountService accountService, CreditService creditService) {
    this.accountService = accountService;
    this.creditService = creditService;
  }

  /**
   * Obtiene los detalles de una cuenta bancaria.
   *
   * @param accountNumber el número de la cuenta bancaria
   * @return un Mono que contiene la respuesta de la cuenta bancaria
   */
  public Mono<AccountResponse> getAccountDetails(String accountNumber) {
    return accountService.getAccount(accountNumber)
        .flatMap(response -> {
          if (!response.isSuccess()) {
            log.warn("Not found account");
            return Mono.error(new NotFoundProduct("Bank account not found"));
          }
          log.info("A bank account was found");
          AccountResponse accountResponse = GenericMapper.mapToAny(response.getData(), AccountResponse.class);
          return Mono.just(accountResponse);
        });
  }

  public Mono<CreditResponse> getCreditDetails(String creditNumber) {
    return creditService.getCredit(creditNumber)
        .flatMap(response -> {
          if (!response.isSuccess()) {
            log.warn("Not found credit");
            return Mono.error(new NotFoundProduct("Credit not found"));
          }
          log.info("A credit was found while searching");
          CreditResponse creditResponse = GenericMapper
              .mapToAny(response.getData(), CreditResponse.class);
          return Mono.just(creditResponse);
        });
  }

  public Mono<CreditResponse> getCreditCardDetails(String creditNumber) {
    return creditService.getCreditCard(creditNumber)
        .flatMap(response -> {
          if (!response.isSuccess()) {
            log.warn("Not found credit card");
            return Mono.error(new NotFoundProduct("Credit card not found"));
          }
          log.info("A credit card was found while searching");
          CreditResponse creditResponse = GenericMapper
              .mapToAny(response.getData(), CreditResponse.class);
          return Mono.just(creditResponse);
        });
  }
}