package project.infrastructure.clientcalls;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import project.infrastructure.clientcalls.responses.ClientResponse;
import project.infrastructure.clientcalls.webclient.WebClientInterface;
import reactor.core.publisher.Mono;

@Component
public class WebClientSrv implements WebClientInterface {
  private final WebClient.Builder webClient;

  public WebClientSrv(WebClient.Builder webClient) {
    this.webClient = webClient;
  }

  @Override
  public Mono<ClientResponse> getClientByiD(String clientId) {
    return webClient
        .build()
        .get()
        .uri("/clients/" + clientId)
        .retrieve()
        .bodyToMono(ClientResponse.class);
  }
}
