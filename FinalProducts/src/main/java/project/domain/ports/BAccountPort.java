package project.domain.ports;

import project.infrastructure.clientCalls.responses.Client;
import reactor.core.publisher.Mono;

public interface BAccountPort {
  Mono<?> save(Object bankAccountDTO, Client client);
}
