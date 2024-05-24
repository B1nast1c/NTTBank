package project.transactionsservice.infrastructure.servicecalls;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import project.transactionsservice.infrastructure.servicecalls.request.AccountRequest;
import project.transactionsservice.infrastructure.servicecalls.responses.GenericResponse;
import project.transactionsservice.infrastructure.servicecalls.webClient.AccountWebInterface;
import reactor.core.publisher.Mono;

/**
 * Servicio para interactuar con el servicio de cuentas bancarias.
 */
@Slf4j
@Component
public class AccountService implements AccountWebInterface {
  private final WebClient.Builder webClient;

  /**
   * Constructor de AccountService.
   *
   * @param webClient constructor de WebClient para realizar llamadas HTTP
   */
  public AccountService(WebClient.Builder webClient) {
    this.webClient = webClient
        .baseUrl("http://accountsService");
  }

  /**
   * Obtiene los detalles de una cuenta bancaria.
   *
   * @param accountNumber el número de la cuenta bancaria
   * @return un Mono que contiene la respuesta genérica de la cuenta bancaria
   */
  @Override
  public Mono<GenericResponse> getAccount(String accountNumber) {
    log.info("Getting account details from the accounts service");
    return webClient
        .build()
        .get()
        .uri("/accounts/account/{accountNumber}", accountNumber)
        .retrieve()
        .bodyToMono(GenericResponse.class);
  }

  /**
   * Actualiza los detalles de una cuenta bancaria.
   *
   * @param accountNumber el número de la cuenta bancaria
   * @param body          el cuerpo de la solicitud que contiene los detalles de la actualización
   * @return un Mono que contiene la respuesta genérica de la cuenta bancaria actualizada
   */
  @Override
  public Mono<GenericResponse> updateAccount(String accountNumber, AccountRequest body) {
    log.info("Updating account details in the accounts service");
    return webClient
        .build()
        .patch()
        .uri("/accounts/update/{accountNumber}", accountNumber)
        .bodyValue(body)
        .retrieve()
        .bodyToMono(GenericResponse.class);
  }
}