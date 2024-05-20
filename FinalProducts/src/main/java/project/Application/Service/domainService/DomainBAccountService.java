package project.Application.Service.domainService;

import org.springframework.stereotype.Service;
import project.Application.Service.BankAccountService;
import project.domain.model.AccountType;
import project.domain.ports.BAccountPort;
import project.infrastructure.adapters.GenericAccAdapter;
import project.infrastructure.clientCalls.WebClientSrv;
import project.infrastructure.clientCalls.responses.Client;
import project.infrastructure.dto.account.BankAccountDTO;
import project.infrastructure.dto.account.LegalSignerDTO;
import project.infrastructure.dto.account.TitularDTO;
import project.infrastructure.factory.BARepoFactory;
import project.infrastructure.mapper.GenericMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DomainBAccountService implements BankAccountService {
  private final BARepoFactory repositoryFactory;
  private final WebClientSrv clientService;
  private final GenericAccAdapter genericRepository;


  public DomainBAccountService(final BARepoFactory repositoryFactory, final WebClientSrv apiCalls, final GenericAccAdapter genericRepository) {
    this.repositoryFactory = repositoryFactory;
    this.clientService = apiCalls;
    this.genericRepository = genericRepository;
  }

  @Override
  public Mono<?> createBankAccount(final Object account) {
    BankAccountDTO bankAccountDTO = GenericMapper.mapToSpecificClass(account, BankAccountDTO.class);
    AccountType accountType = bankAccountDTO.getAccountType();
    String clientId = bankAccountDTO.getClientId();
    BAccountPort repository = repositoryFactory.getAdapter(accountType);
    Mono<Client> foundClientMono = clientService.getClientByiD(clientId);
    return foundClientMono.flatMap(foundClient -> repository.save(account, foundClient));
  }

  @Override
  public Mono<BankAccountDTO> getBankAccount(String accountNumber) {
    return null;
  }

  @Override
  public void updateBankAccount(String accountNumber, Mono<BankAccountDTO> bankAccountDTO) {

  }

  @Override
  public void deleteBankAccount(String accountNumber) {

  }

  @Override
  public Flux<?> getBankAccounts() {
    return genericRepository.findAll();
  }

  @Override
  public Flux<?> getAllBankAccountsByClientId(String clientId) {
    return genericRepository.findByClientId(clientId);
  }

  @Override
  public Mono<BankAccountDTO> addTitularsToAccount(String accountNumber, Flux<TitularDTO> titularsDTO) {
    return null;
  }

  @Override
  public Mono<BankAccountDTO> addLegalSignersToAccount(String accountNumber, Flux<LegalSignerDTO> legalSignersDTO) {
    return null;
  }

  @Override
  public Mono<BankAccountDTO> removeTitularfromAccount(String accountNumber, int titularId) {
    return null;
  }

  @Override
  public Mono<BankAccountDTO> removeLegalSignerfromAccount(String accountNumber, int signerId) {
    return null;
  }
}