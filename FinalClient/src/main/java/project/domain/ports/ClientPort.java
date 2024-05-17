package project.domain.ports;

import project.infrastructure.dto.ClientDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Representa el puerto para interactuar con los datos de los clientes entre las capas de dominio e infraestructura
 * Recibe el nombre de "puerto" ya que conecta la capa de infraestructura con el modelo
 */
public interface ClientPort {
  /**
   * Obtiene un cliente por su ID.
   *
   * @param id el ID del cliente a recuperar
   * @return un Mono emitiendo el cliente, si se encuentra; de lo contrario, un Mono vacío
   */
  Mono<?> findByID(String id);

  /**
   * Guarda un nuevo cliente.
   *
   * @param client el DTO del cliente a guardar
   * @return un Mono emitiendo el DTO del cliente guardado
   */
  Mono<?> save(ClientDTO client);

  /**
   * Obtiene todos los clientes.
   *
   * @return un Flux emitiendo todos los clientes
   */
  Flux<ClientDTO> findAll();

  /**
   * Elimina un cliente por su ID.
   *
   * @param clientId el ID del cliente a eliminar
   * @return un Mono emitiendo una señal de éxito al eliminar
   */
  Mono<?> delete(String clientId);

  /**
   * Actualiza un cliente.
   *
   * @param clientId el ID del cliente a actualizar
   * @param client   el objeto cliente actualizado
   * @return un Mono emitiendo el cliente actualizado
   */
  Mono<?> update(String clientId, Object client);
}
