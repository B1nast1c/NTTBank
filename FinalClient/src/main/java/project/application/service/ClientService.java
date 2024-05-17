package project.application.service;

import project.infrastructure.dto.ClientDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interfaz que define los servicios relacionados con los clientes.
 */
public interface ClientService {

  /**
   * Agrega un nuevo cliente al sistema
   *
   * @param client el DTO del cliente que representa el nuevo cliente
   * @return un Mono que emite un mensaje de confirmación o un error
   */
  Mono<?> addClient(ClientDTO client);

  /**
   * Actualiza un cliente existente en el sistema
   *
   * @param clientId el identificador del cliente que se va a actualizar
   * @param client   un objeto que contiene los datos actualizados del cliente
   * @return un Mono que emite un mensaje de confirmación o un error
   */
  Mono<?> updateClient(String clientId, Object client);

  /**
   * Elimina un cliente existente del sistema
   *
   * @param clientId el identificador del cliente que se va a eliminar
   * @return un Mono que emite un mensaje de confirmación o un error
   */
  Mono<?> deleteClient(String clientId);

  /**
   * Recupera un cliente específico del sistema basado en su identificador
   *
   * @param clientId el identificador del cliente que se va a recuperar
   * @return un Mono que emite el DTO de cliente correspondiente, o un error si el cliente no se encuentra
   */
  Mono<?> getClient(String clientId);

  /**
   * Recupera todos los clientes almacenados en el sistema
   *
   * @return un Flux que emite objetos ClientDTO que representan a todos los clientes en el sistema
   */
  Flux<ClientDTO> getAllClients();
}