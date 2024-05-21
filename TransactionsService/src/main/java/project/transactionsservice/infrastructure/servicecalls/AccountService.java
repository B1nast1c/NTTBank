package project.transactionsservice.infrastructure.servicecalls;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import project.transactionsservice.infrastructure.servicecalls.request.AccountRequest;
import project.transactionsservice.infrastructure.servicecalls.responses.GenericResponse;
import project.transactionsservice.infrastructure.servicecalls.webClient.AccountWebInterface;
import reactor.core.publisher.Mono;

@Component
public class AccountService implements AccountWebInterface {
  private WebClient.Builder webClient;

  public AccountService(WebClient.Builder webClient) {
    this.webClient = webClient
        .baseUrl("http://accountsService");
  }

  @Override
  public Mono<GenericResponse> getAccount(String accountNumber) {
    return webClient
        .build()
        .get()
        .uri("/accounts/account/{accountNumber}", accountNumber)
        .retrieve()
        .bodyToMono(GenericResponse.class);
  }

  @Override
  public Mono<GenericResponse> updateAccount(String accountNumber, AccountRequest body) {
    return webClient
        .build()
        .patch()
        .uri("/accounts/update/{accountNumber}", accountNumber)
        .bodyValue(body)
        .retrieve()
        .bodyToMono(GenericResponse.class);
  }
}
