package project.transactionsservice.infrastructure.servicecalls;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import project.transactionsservice.infrastructure.servicecalls.request.CreditRequest;
import project.transactionsservice.infrastructure.servicecalls.responses.GenericResponse;
import project.transactionsservice.infrastructure.servicecalls.webClient.CreditWebInterface;
import reactor.core.publisher.Mono;

@Component
public class CreditService implements CreditWebInterface {
  private final WebClient.Builder webClient;

  public CreditService(WebClient.Builder webClient) {
    this.webClient = webClient
        .baseUrl("http://microcervices-credit");
  }

  @Override
  public Mono<GenericResponse> getCredit(String creditId) {
    return webClient
        .build()
        .get()
        .uri("/credits/credit/{creditId}", creditId)
        .retrieve()
        .bodyToMono(GenericResponse.class);
  }

  @Override
  public Mono<GenericResponse> updateCredit(String creditId, CreditRequest request) {
    return webClient
        .build()
        .put()
        .uri("/credits/update/" + creditId)
        .bodyValue(request)
        .retrieve()
        .bodyToMono(GenericResponse.class);
  }

  @Override
  public Mono<GenericResponse> getCreditCard(String creditId) {
    return webClient
        .build()
        .get()
        .uri("/cards/card/{creditId}", creditId)
        .retrieve()
        .bodyToMono(GenericResponse.class);
  }

  @Override
  public Mono<GenericResponse> updateCreditCard(String cardNumber, CreditRequest request) {
    return webClient
        .build()
        .put()
        .uri("/cards/update/" + cardNumber)
        .bodyValue(request)
        .retrieve()
        .bodyToMono(GenericResponse.class);
  }
}
