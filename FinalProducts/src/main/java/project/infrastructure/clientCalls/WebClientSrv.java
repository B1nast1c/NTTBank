package project.infrastructure.clientCalls;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import project.infrastructure.clientCalls.responses.Client;
import project.infrastructure.clientCalls.webClient.WebClientInterface;
import reactor.core.publisher.Mono;

@Component
public class WebClientSrv implements WebClientInterface {
  private final WebClient.Builder webClient;

  public WebClientSrv(WebClient.Builder webClient) {
    this.webClient = webClient;
  }

  @Override
  public Mono<Client> getClientByiD(String clientId) {
    return webClient
            .build()
            .get()
            .uri("/clients/" + clientId)
            .retrieve()
            .bodyToMono(Client.class);
  }
}
