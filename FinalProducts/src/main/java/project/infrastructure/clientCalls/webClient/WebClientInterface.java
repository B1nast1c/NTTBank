package project.infrastructure.clientCalls.webClient;

import project.infrastructure.clientCalls.responses.Client;
import reactor.core.publisher.Mono;

public interface WebClientInterface {
  Mono<Client> getClientByiD(String clientId);
}
