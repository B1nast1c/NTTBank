package project.domain.ports;

import project.infrastructure.clientcalls.responses.Client;
import reactor.core.publisher.Mono;

public interface BAccountPort {
  Mono<Object> save(Object bankAccountDTO, Client client);

  Mono<Object> update(Object bankAccountDTO, Object foundAccount);
}
