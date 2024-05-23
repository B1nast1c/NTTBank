package project.application.controller;

import org.springframework.web.bind.annotation.*;
import project.application.service.BankAccountService;
import project.infrastructure.dto.BankAccountDTO;
import project.infrastructure.responses.CustomResponse;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * El tipo Controlador de cuentas bancarias.
 * Esta clase maneja las solicitudes HTTP relacionadas con las cuentas bancarias.
 */
@RestController
@RequestMapping("/accounts")
public class BankAccountController {
  private final BankAccountService bankAccountService;

  /**
   * Instancia un nuevo controlador de cuentas bancarias.
   *
   * @param bankAccountService el servicio de cuentas bancarias
   */
  public BankAccountController(BankAccountService bankAccountService) {
    this.bankAccountService = bankAccountService;
  }

  /**
   * Agrega una nueva cuenta bancaria.
   *
   * @param bankAccount los datos de la cuenta bancaria
   * @return un Mono que contiene la respuesta personalizada con el resultado de la operación
   */
  @PostMapping("/create")
  public Mono<CustomResponse<Object>> addBankAccount(@RequestBody Object bankAccount) {
    return bankAccountService.createBankAccount(bankAccount);
  }

  /**
   * Obtiene todas las cuentas bancarias para un cliente específico por ID de cliente.
   *
   * @param clientId el ID del cliente
   * @return un Mono que contiene la respuesta personalizada con la lista de cuentas bancarias
   */
  @GetMapping("/client/{clientId}")
  public Mono<CustomResponse<Object>> getBankAccountsByClientId
  (@PathVariable("clientId") String clientId) {
    return bankAccountService.getAllBankAccountsByClientId(clientId);
  }

  /**
   * Obtiene una cuenta bancaria específica por ID de cuenta.
   *
   * @param accountId el ID de la cuenta
   * @return un Mono que contiene la respuesta personalizada con los detalles de la cuenta bancaria
   */
  @GetMapping("/account/{accountId}")
  public Mono<CustomResponse<Object>> getBankAccountsById
  (@PathVariable("accountId") String accountId) {
    return bankAccountService.getBankAccount(accountId);
  }

  /**
   * Obtiene el saldo de una cuenta bancaria específica por ID de cuenta.
   *
   * @param accountId el ID de la cuenta
   * @return un Mono que contiene la respuesta personalizada con el saldo de la cuenta
   */
  @GetMapping("/account/balance/{accountId}")
  public Mono<CustomResponse<Object>> getBankAccountBalance
  (@PathVariable("accountId") String accountId) {
    return bankAccountService.getAccountBalance(accountId);
  }

  /**
   * Obtiene todas las cuentas bancarias.
   *
   * @return un Mono que contiene la respuesta personalizada con la lista de todas las cuentas bancarias
   */
  @GetMapping("/all")
  public Mono<CustomResponse<List<Object>>> getAllBankAccounts() {
    return bankAccountService.getBankAccounts();
  }

  /**
   * Actualiza una cuenta bancaria específica.
   *
   * @param accountId   el ID de la cuenta
   * @param bankAccount los datos actualizados de la cuenta bancaria
   * @return un Mono que contiene la respuesta personalizada con el resultado de la operación de actualización
   */
  @PatchMapping("/update/{accountId}")
  public Mono<CustomResponse<Object>> updateBankAccount(
      @PathVariable("accountId") String accountId,
      @RequestBody BankAccountDTO bankAccount) {
    return bankAccountService.updateBankAccount(accountId, bankAccount);
  }
}