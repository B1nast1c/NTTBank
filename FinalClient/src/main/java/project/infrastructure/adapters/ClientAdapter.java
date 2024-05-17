package project.infrastructure.adapters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.domain.model.Client;
import project.domain.model.ClientType;
import project.domain.ports.ClientPort;
import project.infrastructure.adapters.mongoRepos.ClientRepository;
import project.infrastructure.dto.ClientDTO;
import project.infrastructure.exceptions.CustomError;
import project.infrastructure.mapper.GenericMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Repository
public class ClientAdapter implements ClientPort {
  private final ClientRepository clientRepository;

  /**
   * Constructor para la clase ClientAdapter
   * (ADAPTER -> Implementación de la conexión entre DOMAIN y INFRASTRUCTURE)
   *
   * @param clientRepository El repositorio para las operaciones de cliente
   */
  public ClientAdapter(final ClientRepository clientRepository) {
    this.clientRepository = clientRepository;
  }

  @Override
  public Mono<?> findByID(final String documentNumber) {
    return clientRepository.findByDocumentNumber(documentNumber).flatMap(client -> {
      Optional<Client> optionalClient = Optional.ofNullable(client);
      return Mono.justOrEmpty(optionalClient).map(GenericMapper::mapToDto); // En caso no encontrarlo, se retorna un elemento vacío
    });
  }

  @Override
  public Mono<?> save(final ClientDTO clientDTO) {
    return Mono.just(clientDTO).map(GenericMapper::mapToEntity).flatMap(client -> {
      if (client.getClientType() != null && (client.getClientType().equals(ClientType.PERSONAL)
          || client.getClientType().equals(ClientType.EMPRESARIAL))) {

        if (client.getDocumentNumber() != null && !client.getDocumentNumber().isEmpty()) {

          // Inserta el cliente en el repositorio y mapea el cliente insertado a su representación DTO.
          return clientRepository.insert(client).map(insertedClient -> {
            ClientDTO DTO = GenericMapper.mapToDto(insertedClient);
            DTO.setStatus(insertedClient.getStatus());
            DTO.setCreatedAt(insertedClient.getCreatedAt());

            log.debug("Cliente creado: ", DTO);
            return DTO;
          });
        } else {
          log.warn("\033[1;31mNo se proporcionó el número de documento\033[0m");
          return Mono.just(new CustomError("El documento del cliente no puede estar vacío", CustomError.Type.INVALID_DOCUMENT));
        }
      } else {
        log.warn("\033[1;31mNo se proporcionó el tipo de cliente o el tipo proporcionado es incorrecto\033[0m");
        return Mono.just(new CustomError("El tipo de cliente debe ser PERSONAL o EMPRESARIAL", CustomError.Type.INVALID_TYPE));
      }
    }).onErrorResume(error -> {
      log.warn("\033[1;31mAlgunos campos proporcionados por el cliente son incorrectos o faltan\033[0m");
      return Mono.just(new CustomError("Algunos campos son incorrectos o faltan", CustomError.Type.GENERIC_ERROR));
    });
  }


  @Override
  public Flux<ClientDTO> findAll() {
    log.info("Buscando todos los clientes desde el ADAPTADOR");
    return clientRepository.findAll().map(GenericMapper::mapToDto);
  }

  @Override
  public Mono<?> delete(final String clientId) {
    log.info("Operación de eliminación desde el ADAPTADOR");
    return clientRepository.existsByCustomId(clientId).flatMap(exists -> { // Valida la existencia lógica del cliente
      if (exists) {
        log.info("Eliminando cliente {} desde el ADAPTADOR", clientId);
        return clientRepository.deleteById(clientId).then(Mono.just("Cliente eliminado correctamente"));
      } else {
        return Mono.just(new CustomError("El cliente con ID: " + clientId + " no existe", CustomError.Type.NOT_FOUND));
      }
    });
  }

  @Transactional
  @Override
  public Mono<?> update(final String clientId, final Object client) {
    return clientRepository.existsByCustomId(clientId).flatMap(exists -> {
      if (exists) {
        // Mapea el objeto cliente actualizado a su representación DTO.
        ClientDTO DTO = GenericMapper.mapToDto(client);

        return clientRepository.findById(clientId).flatMap(existingClient -> {
          // Actualiza el cliente existente con los nuevos datos.
          existingClient.setClientName(DTO.getClientName());
          existingClient.setClientAddress(DTO.getClientAddress());
          existingClient.setClientPhone(DTO.getClientPhone());
          existingClient.setClientEmail(DTO.getClientEmail());
          existingClient.setStatus(DTO.getStatus());

          log.debug("Cliente actualizado: ", existingClient);

          return clientRepository.save(existingClient).then(Mono.just("Cliente actualizado correctamente"));
        });
      } else {
        log.warn("\033[1;31mNo existe ningún cliente con el ID {}\033[0m", clientId);
        return Mono.just(new CustomError("El cliente con ID: " + clientId + " no existe", CustomError.Type.NOT_FOUND));
      }
    }).onErrorResume(error -> {
      log.warn("\033[1;31mSe produjo un error al persistir el cliente\033[0m");
      return Mono.just(new CustomError("Algunos campos son incorrectos o faltan", CustomError.Type.GENERIC_ERROR));
    });
  }
}
