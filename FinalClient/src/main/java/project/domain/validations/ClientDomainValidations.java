package project.domain.validations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.infrastructure.dto.ClientDTO;
import project.infrastructure.exceptions.CustomError;
import project.infrastructure.exceptions.throwable.WrongClientType;
import reactor.core.publisher.Mono;

/**
 * Validaciones a nivel de dominio, incluye todas las reglas de negocio.
 */
@Slf4j
@Component
public class ClientDomainValidations {
  /**
   * Valida el tipo de cliente (PERSONAL o EMPRESARIAL).
   *
   * @param client El DTO del cliente.
   * @return Dependiendo del cumplimiento, se retorna el mismo elemento
   * o una excepción.
   */
  public Mono<ClientDTO> validateClientType(ClientDTO client) {
    if (!client.getClientType().equalsIgnoreCase("PERSONAL")
        && !client.getClientType().equalsIgnoreCase("EMPRESARIAL")) {
      log.warn("Client type is not valid -> {}", CustomError.ErrorType.INVALID_TYPE);
      return Mono.error(() -> new WrongClientType("Client type must be PERSONAL or EMPRESARIAL"));
    }
    return Mono.just(client);
  }
}
