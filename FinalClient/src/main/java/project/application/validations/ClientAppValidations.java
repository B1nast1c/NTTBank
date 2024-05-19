package project.application.validations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.infrastructure.adapters.mongoRepos.ClientRepository;
import project.infrastructure.dto.ClientDTO;
import project.infrastructure.exceptions.CustomError;
import project.infrastructure.exceptions.throwable.InvalidDocument;
import reactor.core.publisher.Mono;

/**
 * Clase que implementa las validaciones a nivel de aplicación,
 * son errores humanos como campos nulos o valores de tipo que no le corresponde.
 */
@Slf4j
@Component
public class ClientAppValidations {
  private final ClientRepository clientRepository;

  /**
   * Constructor de la clase.
   *
   * @param clientRepository Repositorio para la validación.
   */
  public ClientAppValidations(ClientRepository clientRepository) {
    this.clientRepository = clientRepository;
  }

  /**
   * Valida que el DNI o el RUC sea válido: Se encuentra vacío o es un elemento repetido.
   *
   * @param client Cliente
   * @return El cliente si el conjunto es correcto, y si no alguna excepción personalizada.
   */
  public Mono<ClientDTO> validateDocumentNumber(ClientDTO client) {
    return clientRepository.existsByDocumentNumber(client.getDocumentNumber())
        .flatMap(foundClient -> {
              if (Boolean.TRUE.equals(foundClient)) {
                log.warn("Document number is empty or duplicated -> {}",
                    CustomError.ErrorType.INVALID_DOCUMENT);
                return Mono.error(
                    () -> new InvalidDocument("The provided document number is not valid")
                );
              }
              log.info("The client has been validated");
              return Mono.just(client);
            }
        );
  }
}
