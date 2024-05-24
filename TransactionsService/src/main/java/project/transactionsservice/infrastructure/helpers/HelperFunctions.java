package project.transactionsservice.infrastructure.helpers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.transactionsservice.infrastructure.exceptions.throwable.NotFoundProduct;
import project.transactionsservice.infrastructure.mapper.GenericMapper;
import project.transactionsservice.infrastructure.servicecalls.AccountService;
import project.transactionsservice.infrastructure.servicecalls.responses.AccountResponse;
import reactor.core.publisher.Mono;

/**
 * Clase HelperFunctions que contiene funciones auxiliares para el servicio de cuentas.
 */
@Slf4j
@Component
public class HelperFunctions {
  private final AccountService accountService;

  /**
   * Constructor de HelperFunctions.
   *
   * @param accountService servicio de cuentas para obtener detalles de la cuenta
   */
  public HelperFunctions(AccountService accountService) {
    this.accountService = accountService;
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
}