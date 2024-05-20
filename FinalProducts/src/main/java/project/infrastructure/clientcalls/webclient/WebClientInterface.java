package project.infrastructure.clientcalls.webclient;

import project.infrastructure.clientcalls.responses.ClientResponse;
import reactor.core.publisher.Mono;

public interface WebClientInterface {
  Mono<ClientResponse> getClientByiD(String clientId);
}
