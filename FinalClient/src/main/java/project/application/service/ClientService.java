package project.application.service;

import project.infrastructure.dto.ClientDTO;
import project.infrastructure.responses.CustomResponse;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Interfaz o estructura base del servicio de clientes.
 * Todos los resultados proporcionados por la implementación del servicio
 * se encuentran dentro de una estructura genérica que almacena el resultado
 * sea de éxito o falla.
 */
public interface ClientService {

  /**
   * Agrega un nuevo cliente.
   *
   * @param client el DTO del cliente que representa el nuevo cliente.
   * @return un Mono que emite un mensaje de confirmación o un error.
   */
  Mono<CustomResponse> addClient(ClientDTO client);

  /**
   * Actualiza un cliente existente en el sistema.
   *
   * @param clientId el identificador del cliente que se va a actualizar.
   * @param client   un objeto que contiene los datos actualizados del cliente.
   * @return un Mono que emite un mensaje de confirmación o un error.
   */
  Mono<CustomResponse> updateClient(String clientId, Object client);

  /**
   * Elimina un cliente.
   *
   * @param clientId el identificador del cliente que se va a eliminar.
   * @return un Mono que emite un mensaje de confirmación o un error.
   */
  Mono<CustomResponse> deleteClient(String clientId);

  /**
   * Recupera un cliente específico.
   *
   * @param clientId el identificador del cliente que se va a recuperar.
   * @return un Mono que emite el DTO de cliente correspondiente,
   * o un error si el cliente no se encuentra.
   */
  Mono<CustomResponse> getClient(String clientId);

  /**
   * Recupera todos los clientes almacenados en el sistema.
   *
   * @return un Flux que emite objetos ClientDTO que representan a todos los clientes en el sistema.
   */
  Mono<CustomResponse<List<ClientDTO>>> getAllClients();
}