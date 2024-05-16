package project.infrastructure.controller;

import org.springframework.web.bind.annotation.*;
import project.Application.Service.BankAccountService;
import project.infrastructure.dto.account.BankAccountDTO;
import project.infrastructure.dto.account.LegalSignerDTO;
import project.infrastructure.dto.account.TitularDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SuppressWarnings({"ALL", "SpellCheckingInspection"})
@RestController
@RequestMapping("/accounts")
public class BankAccountController {
  private final BankAccountService bankAccountService;

  public BankAccountController(BankAccountService bankAccountService) {
    this.bankAccountService = bankAccountService;
  }

  @PostMapping
  public Mono<?> addBankAccount(@RequestBody Object bankAccount) {
    return bankAccountService.createBankAccount(bankAccount);
  }

  @GetMapping("/client/{clientId}")
  public Flux<?> getBankAccountsByClientId(@PathVariable("clientId") String clientId) {
    return bankAccountService.getAllBankAccountsByClientId(clientId);
  }

  @GetMapping("/{accountId}")
  public Mono<?> getBankAccountsById(@PathVariable("accountId") String accountId) {
    return bankAccountService.getBankAccount(accountId);
  }

  @GetMapping
  public Flux<?> getAllBankAccounts() {
    return bankAccountService.getBankAccounts();
  }

  @PostMapping("/titulars/{accountId}")
  public Mono<?> addTitularsToAccount(@PathVariable("accountId") String accountId, @RequestBody Flux<TitularDTO> titulars) {
    return bankAccountService.addTitularsToAccount(accountId, titulars);
  }

  @DeleteMapping("/titulars/{accountId}/{ownerId}")
  public Mono<BankAccountDTO> removeTitulars(@PathVariable("accountId") String accountId, @PathVariable("ownerId") int ownerId) {
    return bankAccountService.removeTitularfromAccount(accountId, ownerId);
  }

  @PostMapping("/signers/{accountId}")
  public Mono<?> addSignersToAccount(@PathVariable("accountId") String accountId, @RequestBody Flux<LegalSignerDTO> signers) {
    return bankAccountService.addLegalSignersToAccount(accountId, signers);
  }

  @DeleteMapping("/signers/{accountId}/{ownerId}")
  public Mono<BankAccountDTO> removeLegalSigners(@PathVariable("accountId") String accountId, @PathVariable("ownerId") int ownerId) {
    return bankAccountService.removeLegalSignerfromAccount(accountId, ownerId);
  }
}
