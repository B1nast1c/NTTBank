package project.Domain.Ports;

// Recibe el nombre de puerto, pues es el que contecta a la capa de infraestructura con el modelo

import project.Infrastructure.DTO.ClientDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientPort {
    Mono<ClientDTO> findByID(String id);

    Mono<ClientDTO> save(Mono<ClientDTO> client);

    Flux<ClientDTO> findAll();

    Mono<Void> delete(String clientId);
}
