package project.domain.ports;

import project.infrastructure.clientcalls.responses.Client;
import reactor.core.publisher.Mono;

/**
 * La interfaz BAccountPort define los métodos que deben ser implementados
 * por los adaptadores para interactuar con las cuentas bancarias.
 */
public interface BAccountPort {
  /**
   * Guarda una cuenta bancaria.
   *
   * @param bankAccountDTO El DTO de la cuenta bancaria a guardar.
   * @param client         El cliente asociado a la cuenta bancaria.
   * @return Un Mono que representa el resultado de la operación de guardado.
   */
  Mono<Object> save(Object bankAccountDTO, Client client);

  /**
   * Actualiza una cuenta bancaria.
   *
   * @param bankAccountDTO El DTO de la cuenta bancaria actualizada.
   * @param foundAccount   La cuenta bancaria encontrada que será actualizada.
   * @return Un Mono que representa el resultado de la operación de actualización.
   */
  Mono<Object> update(Object bankAccountDTO, Object foundAccount);
}