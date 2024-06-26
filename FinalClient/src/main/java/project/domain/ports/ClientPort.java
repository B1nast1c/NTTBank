package project.domain.ports;

import project.infrastructure.dto.ClientDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Representa el puerto para interactuar con cada elemento de la entidad CLIENTE
 * entre las capas de dominio e infraestructura. Recibe el nombre de "puerto"
 * ya que conecta la capa de infraestructura con el modelo.
 */
public interface ClientPort {
  Mono<ClientDTO> findByID(String id);

  Mono<ClientDTO> save(ClientDTO client);

  Flux<ClientDTO> findAll();

  Mono<String> delete(String clientId);

  Mono<String> update(String clientId, Object client);
}
