package project.Application.Service;

import project.Infrastructure.DTO.ClientDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientService {
    Mono<ClientDTO> addClient(Mono<ClientDTO> client);

    Mono<Void> updateClient(String clientId, Mono<ClientDTO> client);

    Mono<Void> deleteClient(String clientId);

    Mono<ClientDTO> getClient(String clientId);

    Flux<ClientDTO> getAllClients();
}
