package project.application.service.DomainService;

import org.springframework.stereotype.Service;
import project.application.service.ClientService;
import project.domain.ports.ClientPort;
import project.infrastructure.dto.ClientDTO;
import project.infrastructure.exceptions.CustomError;
import project.infrastructure.mapper.GenericMapper;
import project.infrastructure.responses.CustomResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Implementación de dominio de la interfaz ClientService.
 */
@Service
public class DomainClientService implements ClientService {
  private final ClientPort clientPort;

  /**
   * Constructor de DomainClientService.
   *
   * @param port Llama a la implementación del repositorio (PUERTO).
   */
  public DomainClientService(final ClientPort port) {
    this.clientPort = port;
  }

  /**
   * Agrega un nuevo cliente al sistema.
   *
   * @param clientDto El DTO del cliente que representa el nuevo cliente.
   * @return un Mono que emite un mensaje de confirmación o un error.
   */
  @Override
  public Mono<CustomResponse> addClient(final ClientDTO clientDto) {
    return clientPort.save(clientDto)
        .flatMap(foundClient -> {
          CustomResponse response = new CustomResponse<>(true, foundClient);
          return Mono.just(response);
        })
        .onErrorResume(throwable -> {
          CustomError error = new CustomError(throwable.getMessage(), CustomError.ErrorType.GENERIC_ERROR);
          CustomResponse<CustomError> badResponse = new CustomResponse<>(false, error);
          return Mono.just(badResponse);
        });
  }

  /**
   * Actualiza un cliente existente en el sistema.
   *
   * @param clientId el identificador del cliente que se va a actualizar.
   * @param client   un objeto que contiene los datos actualizados del cliente.
   * @return un Mono que emite un mensaje de confirmación o un error.
   */
  @Override
  public Mono<CustomResponse> updateClient(final String clientId, final Object client) {
    return clientPort.update(clientId, client)
        .flatMap(updatedClient -> {
          CustomResponse response = new CustomResponse<>(true, "Client updated successfully");
          return Mono.just(response);
        })
        .onErrorResume(throwable -> {
          CustomError error = new CustomError(throwable.getMessage(), CustomError.ErrorType.GENERIC_ERROR);
          CustomResponse<CustomError> badResponse = new CustomResponse<>(false, error);
          return Mono.just(badResponse);
        });
  }

  /**
   * Elimina un cliente existente del sistema.
   *
   * @param clientId el identificador del cliente que se va a eliminar.
   * @return un Mono que emite un mensaje de confirmación o un error.
   */
  @Override
  public Mono<CustomResponse> deleteClient(final String clientId) {
    return clientPort.delete(clientId)
        .flatMap(deletedClient -> {
          CustomResponse response = new CustomResponse<>(true, deletedClient);
          return Mono.just(response);
        })
        .onErrorResume(throwable -> {
          CustomError error = new CustomError(throwable.getMessage(), CustomError.ErrorType.GENERIC_ERROR);
          CustomResponse<CustomError> badResponse = new CustomResponse<>(false, error);
          return Mono.just(badResponse);
        });
  }


  /**
   * Recupera un cliente específico del sistema basado en su identificador.
   *
   * @param clientId el identificador del cliente que se va a recuperar.
   * @return un Mono que emite el DTO de cliente correspondiente, o un error si el cliente no se encuentra.
   */
  @Override
  public Mono<CustomResponse> getClient(final String clientId) {
    return clientPort.findByID(clientId)
        .flatMap(foundClient -> {
          CustomResponse response = new CustomResponse<>(true, foundClient);
          return Mono.just(response);
        })
        .onErrorResume(throwable -> {
          CustomError error = new CustomError(throwable.getMessage(), CustomError.ErrorType.GENERIC_ERROR);
          CustomResponse<CustomError> badResponse = new CustomResponse<>(false, error);
          return Mono.just(badResponse);
        });
  }

  /**
   * Recupera todos los clientes almacenados en el sistema.
   *
   * @return un Flux que emite objetos ClientDTO que representan a todos los clientes en el sistema.
   */
  @Override
  public Mono<CustomResponse<List<ClientDTO>>> getAllClients() {
    Flux<ClientDTO> clientFlux = clientPort.findAll().map(GenericMapper::mapToDto);
    Mono<List<ClientDTO>> clientListMono = clientFlux.collectList();
    return clientListMono.map(clientList -> new CustomResponse<>(true, clientList));
  }
}