package project.application.service;

import project.infrastructure.dto.BankAccountDTO;
import project.infrastructure.responses.CustomResponse;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * La interfaz Servicio de cuenta bancaria.
 */
public interface BankAccountService {
  /**
   * Crea una cuenta bancaria.
   *
   * @param bankAccountDTO el DTO de la cuenta bancaria
   * @return un flujo con la respuesta personalizada de la operación de creación
   */
  Mono<CustomResponse<Object>> createBankAccount(Object bankAccountDTO);

  /**
   * Obtiene una cuenta bancaria por número de cuenta.
   *
   * @param accountNumber el número de cuenta
   * @return un flujo con la respuesta personalizada de los detalles de la cuenta bancaria
   */
  Mono<CustomResponse<Object>> getBankAccount(String accountNumber);

  /**
   * Actualiza una cuenta bancaria.
   *
   * @param accountNumber  el número de cuenta
   * @param bankAccountDTO el DTO de la cuenta bancaria
   * @return un flujo con la respuesta personalizada del resultado de la operación de actualización
   */
  Mono<CustomResponse<Object>> updateBankAccount(String accountNumber, BankAccountDTO bankAccountDTO);

  /**
   * Obtiene todas las cuentas bancarias.
   *
   * @return un flujo con la respuesta personalizada con la lista de todas las cuentas bancarias
   */
  Mono<CustomResponse<List<Object>>> getBankAccounts();

  /**
   * Obtiene todas las cuentas bancarias por ID de cliente.
   *
   * @param clientId el ID del cliente
   * @return un flujo con la respuesta personalizada con la lista de cuentas bancarias del cliente
   */
  Mono<CustomResponse<Object>> getAllBankAccountsByClientId(String clientId);

  /**
   * Obtiene el saldo de una cuenta bancaria.
   *
   * @param accountNumber el número de cuenta
   * @return un flujo con la respuesta personalizada con el saldo de la cuenta
   */
  Mono<CustomResponse<Object>> getAccountBalance(String accountNumber);
}