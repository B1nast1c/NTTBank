package project.application.service.DomainService;

import org.springframework.stereotype.Service;
import project.application.service.ClientService;
import project.domain.ports.ClientPort;
import project.infrastructure.dto.ClientDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementación de dominio de la interfaz ClientService.
 */
@Service
public class DomainClientService implements ClientService {
  private final ClientPort clientPort;

  /**
   * Constructor de DomainClientService.
   *
   * @param clientPort el puerto del cliente
   */
  public DomainClientService(final ClientPort clientPort) {
    this.clientPort = clientPort;
  }

  /**
   * Agrega un nuevo cliente al sistema
   *
   * @param clientDTO el DTO del cliente que representa el nuevo cliente
   * @return un Mono que emite un mensaje de confirmación o un error
   */
  @Override
  public Mono<?> addClient(final ClientDTO clientDTO) {
    return clientPort.save(clientDTO);
  }

  /**
   * Actualiza un cliente existente en el sistema
   *
   * @param clientId el identificador del cliente que se va a actualizar
   * @param client   un objeto que contiene los datos actualizados del cliente
   * @return un Mono que emite un mensaje de confirmación o un error
   */
  @Override
  public Mono<?> updateClient(final String clientId, final Object client) {
    return clientPort.update(clientId, client);
  }

  /**
   * Elimina un cliente existente del sistema
   *
   * @param clientId el identificador del cliente que se va a eliminar
   * @return un Mono que emite un mensaje de confirmación o un error
   */
  @Override
  public Mono<?> deleteClient(final String clientId) {
    return clientPort.delete(clientId);
  }

  /**
   * Recupera un cliente específico del sistema basado en su identificador
   *
   * @param clientId el identificador del cliente que se va a recuperar
   * @return un Mono que emite el DTO de cliente correspondiente, o un error si el cliente no se encuentra
   */
  @Override
  public Mono<?> getClient(final String clientId) {
    return clientPort.findByID(clientId);
  }

  /**
   * Recupera todos los clientes almacenados en el sistema
   *
   * @return un Flux que emite objetos ClientDTO que representan a todos los clientes en el sistema
   */
  @Override
  public Flux<ClientDTO> getAllClients() {
    return clientPort.findAll();
  }
}