package project.infrastructure.clientcalls;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import project.infrastructure.clientcalls.responses.ClientResponse;
import project.infrastructure.clientcalls.webclient.WebClientInterface;
import reactor.core.publisher.Mono;

/**
 * Componente que realiza llamadas a un servicio de cliente utilizando WebClient.
 */
@Slf4j
@Component
public class WebClientSrv implements WebClientInterface {
  private final WebClient.Builder webClient;

  /**
   * Constructor para inicializar WebClient.Builder.
   *
   * @param webClient Builder para crear instancias de WebClient.
   */
  public WebClientSrv(WebClient.Builder webClient) {
    this.webClient = webClient
        .baseUrl("http://clientService");
  }

  /**
   * Obtiene un cliente por su ID utilizando WebClient.
   *
   * @param clientId ID del cliente a buscar.
   * @return Un Mono que emite una respuesta de cliente.
   */
  @Override
  public Mono<ClientResponse> getClientByiD(String clientId) {
    log.info("Calling ClientService to get client by id");

    return webClient
        .build()
        .get()
        .uri("/clients/" + clientId)
        .retrieve()
        .bodyToMono(ClientResponse.class);
  }
}