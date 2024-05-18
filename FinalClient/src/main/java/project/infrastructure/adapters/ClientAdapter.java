package project.infrastructure.adapters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.application.validations.ClientAppValidations;
import project.domain.ports.ClientPort;
import project.domain.validations.ClientDomainValidations;
import project.infrastructure.adapters.mongoRepos.ClientRepository;
import project.infrastructure.dto.ClientDTO;
import project.infrastructure.exceptions.CustomError;
import project.infrastructure.exceptions.throwable.NotFound;
import project.infrastructure.mapper.GenericMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Adaptador del puerto cliente, ubicado en el paquete de DOMAIN,
 * un adaptador es básicamente la implementación de un repositorio,
 * pero usando un repositorio ya implementado (REACTIVE MONGO).
 */
@Slf4j
@Repository
public class ClientAdapter implements ClientPort {
  private final ClientRepository clientRepository;
  private final ClientAppValidations appValidations;
  private final ClientDomainValidations domainValidations;

  /**
   * Constructor para la clase ClientAdapter
   * (ADAPTER -> Implementación de la conexión entre DOMAIN y INFRASTRUCTURE)
   *
   * @param clientRepository  El repositorio para las operaciones de cliente
   * @param appValidations    Validaciones a nivel de aplicación
   * @param domainValidations Validaciones a nivel de dominio
   */
  public ClientAdapter(final ClientRepository clientRepository, ClientAppValidations appValidations, ClientDomainValidations domainValidations) {
    this.clientRepository = clientRepository;
    this.appValidations = appValidations;
    this.domainValidations = domainValidations;
  }

  /**
   * Busca un cliente por su número de documento.
   *
   * @param documentNumber Número de documento del cliente a buscar.
   * @return Un Mono que emite el cliente encontrado o lanza una excepción si no se encuentra.
   */
  @Override
  public Mono<ClientDTO> findByID(String documentNumber) {
    return clientRepository.findByDocumentNumber(documentNumber)
        .flatMap(client -> {
          log.info("Client found -> {}", client.getClientName());
          return Mono.just(GenericMapper.mapToDto(client));
        }).switchIfEmpty(
            Mono.defer(() -> {
              log.warn("Client not found -> {}", CustomError.ErrorType.NOT_FOUND);
              return Mono.error(new NotFound("Client with document number " + documentNumber + " not found"));
            }));
  }

  /**
   * Valida el tipo de cliente y el número de documento antes de insertar los datos del cliente en la base de datos.
   *
   * @param client Objeto ClientDTO que contiene los datos del cliente a insertar.
   * @return Un Mono que representa la inserción exitosa del cliente o lanza una excepción si hay errores.
   */
  private Mono<?> validateAndInsertClient(ClientDTO client) {
    return domainValidations
        .validateClientType(client)
        .flatMap(item -> appValidations.validateDocumentNumber(client))
        .map(GenericMapper::mapToEntity)
        .flatMap(clientRepository::insert);
  }

  /**
   * Guarda los datos del cliente después de validar el tipo de cliente y el número de documento.
   *
   * @param client Objeto ClientDTO que contiene los datos del cliente a guardar.
   * @return Un Mono que representa la inserción exitosa del cliente o lanza una excepción si hay errores.
   */
  @Override
  public Mono<ClientDTO> save(ClientDTO client) {
    return validateAndInsertClient(client)
        .map(GenericMapper::mapToDto);
  }

  /**
   * Recupera todos los clientes de la base de datos y los devuelve como un Flux de ClientDTO.
   *
   * @return Un Flux que emite los clientes encontrados.
   */
  @Override
  public Flux<ClientDTO> findAll() {
    Flux<ClientDTO> clients = clientRepository.findAll().map(GenericMapper::mapToDto);
    log.info("Found clients -> {}", clients);
    return clients;
  }

  /**
   * Verifica si un cliente existe por su ID y lo elimina si es así.
   *
   * @param clientId ID del cliente a eliminar.
   * @return Un Mono que representa la eliminación exitosa del cliente o lanza una excepción si no se encuentra.
   */
  @Override
  public Mono<String> delete(final String clientId) {
    return clientRepository.existsById(clientId)
        .flatMap(exists -> {
          if (Boolean.TRUE.equals(exists)) {
            log.info("Client to delete -> {}", clientId);
            return clientRepository
                .deleteById(clientId)
                .then(Mono.just("Client deleted successfully"));
          } else {
            log.warn("Deleting client not found -> {}", CustomError.ErrorType.NOT_FOUND);
            return Mono.error(new NotFound("Client with ID " + clientId + " not found"));
          }
        });
  }

  /**
   * Actualiza los datos de un cliente existente en la base de datos.
   *
   * @param clientId  ID del cliente a actualizar.
   * @param clientDTO Objeto ClientDTO con los nuevos datos del cliente.
   * @return Un Mono que representa la actualización exitosa del cliente o lanza una excepción si hay errores.
   */
  private Mono<?> updateExistingClient(String clientId, ClientDTO clientDTO) {
    return clientRepository.findById(clientId).flatMap(existingClient -> {
      existingClient.setClientName(clientDTO.getClientName());
      existingClient.setClientAddress(clientDTO.getClientAddress());
      existingClient.setClientPhone(clientDTO.getClientPhone());
      existingClient.setClientEmail(clientDTO.getClientEmail());
      existingClient.setStatus(clientDTO.getStatus());

      log.info("Updating client{}", existingClient.getClientName());

      return clientRepository.save(existingClient);
    });
  }

  /**
   * Verifica si un cliente existe por su ID y lo actualiza si es así.
   *
   * @param clientId ID del cliente a actualizar.
   * @param client   Objeto con los nuevos datos del cliente.
   * @return Un Mono que representa la actualización exitosa del cliente o lanza una excepción si no se encuentra.
   */
  @Override
  @Transactional
  public Mono<String> update(String clientId, Object client) {
    return clientRepository.existsByCustomId(clientId)
        .flatMap(exists -> {
          if (Boolean.TRUE.equals(exists)) {
            log.info("Client to update -> {}", clientId);
            return updateExistingClient(clientId, GenericMapper.mapToDto(client))
                .then(Mono.just("Client updated successfully"));
          } else {
            log.warn("Not found client -> {}", CustomError.ErrorType.NOT_FOUND);
            return Mono.error(new NotFound("Looking for a client that does not exist"));
          }
        });
  }
}