package project.application.controller;

import org.springframework.web.bind.annotation.*;
import project.application.service.BankAccountService;
import project.infrastructure.dto.BankAccountDTO;
import project.infrastructure.dto.CurrAccDTO;
import project.infrastructure.dto.LegalSignerDTO;
import project.infrastructure.responses.CustomResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class BankAccountController {
  private final BankAccountService bankAccountService;

  public BankAccountController(BankAccountService bankAccountService) {
    this.bankAccountService = bankAccountService;
  }

  @PostMapping("/create")
  public Mono<CustomResponse<Object>> addBankAccount(@RequestBody Object bankAccount) {
    return bankAccountService.createBankAccount(bankAccount);
  }

  @GetMapping("/client/{clientId}")
  public Mono<CustomResponse<Object>> getBankAccountsByClientId(@PathVariable("clientId") String clientId) {
    return bankAccountService.getAllBankAccountsByClientId(clientId);
  }

  @GetMapping("/account/{accountId}")
  public Mono<CustomResponse<Object>> getBankAccountsById(@PathVariable("accountId") String accountId) {
    return bankAccountService.getBankAccount(accountId);
  }

  @GetMapping("/account/balance/{accountId}")
  public Mono<CustomResponse<Object>> getBankAccountBalance(@PathVariable("accountId") String accountId) {
    return bankAccountService.getAccountBalance(accountId);
  }

  @GetMapping("/all")
  public Mono<CustomResponse<List<Object>>> getAllBankAccounts() {
    return bankAccountService.getBankAccounts();
  }

  @PatchMapping("/update/{accountId}")
  public Mono<CustomResponse<Object>> updateBankAccount(
      @PathVariable("accountId") String accountId,
      @RequestBody BankAccountDTO bankAccount) {
    return bankAccountService.updateBankAccount(accountId, bankAccount);
  }

  @PostMapping("/titulars/{accountId}")
  public Mono<CustomResponse<CurrAccDTO>> addTitularsToAccount(
      @PathVariable("accountId") String accountId,
      @RequestBody List<String> titulars) {
    return bankAccountService.addTitularsToAccount(accountId, titulars);
  }

  @DeleteMapping("/titulars/{accountId}/{ownerId}")
  public Mono<CustomResponse<CurrAccDTO>> removeTitulars(
      @PathVariable("accountId") String accountId,
      @PathVariable("ownerId") String ownerId) {
    return bankAccountService.removeTitularfromAccount(accountId, ownerId);
  }

  @PostMapping("/signers/{accountId}")
  public Mono<CustomResponse<CurrAccDTO>> addSignersToAccount(
      @PathVariable("accountId") String accountId,
      @RequestBody List<LegalSignerDTO> signers) {
    return bankAccountService.addLegalSignersToAccount(accountId, signers);
  }

  @DeleteMapping("/signers/{accountId}/{ownerId}")
  public Mono<CustomResponse<CurrAccDTO>> removeLegalSigners(
      @PathVariable("accountId") String accountId,
      @PathVariable("ownerId") String ownerId) {
    return bankAccountService.removeLegalSignerfromAccount(accountId, ownerId);
  }
}
