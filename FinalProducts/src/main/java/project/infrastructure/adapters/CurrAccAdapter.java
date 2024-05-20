package project.infrastructure.adapters;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import project.domain.model.account.CurrentAccount;
import project.domain.model.account.LegalSigner;
import project.domain.ports.BAccountPort;
import project.domain.validations.SaveDomainValidations;
import project.domain.validations.UpdateDomainValidations;
import project.infrastructure.adapters.mongorepo.CurrAccRepo;
import project.infrastructure.clientcalls.WebClientSrv;
import project.infrastructure.clientcalls.responses.Client;
import project.infrastructure.clientcalls.responses.ClientResponse;
import project.infrastructure.dto.CurrAccDTO;
import project.infrastructure.exceptions.throwable.EmptyAttributes;
import project.infrastructure.exceptions.throwable.InvalidRule;
import project.infrastructure.mapper.GenericMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class CurrAccAdapter implements BAccountPort {
  private final CurrAccRepo currentRepo;
  private final SaveDomainValidations domainValidations;
  private final WebClientSrv clientService;
  private final UpdateDomainValidations updateDomainValidations;
  private final ReactiveMongoTemplate reactiveMongoTemplate;

  public CurrAccAdapter(CurrAccRepo currentRepo,
                        SaveDomainValidations domainValidations,
                        WebClientSrv clientService,
                        UpdateDomainValidations updateDomainValidations,
                        ReactiveMongoTemplate reactiveMongoTemplate) {
    this.currentRepo = currentRepo;
    this.domainValidations = domainValidations;
    this.clientService = clientService;
    this.updateDomainValidations = updateDomainValidations;
    this.reactiveMongoTemplate = reactiveMongoTemplate;
  }

  private CurrentAccount convertClass(Object bankAccountDTO) {
    return GenericMapper.mapToSpecificClass(bankAccountDTO, CurrentAccount.class);
  }

  private CurrAccDTO customDTO(Object currAccount) {
    return GenericMapper.mapToSpecificClass(currAccount, CurrAccDTO.class);
  }

  private Mono<Set<String>> validateAndFilterTitulars(Set<String> titulars) {
    return Flux.fromIterable(titulars)
        .flatMap(titular -> clientService.getClientByiD(titular)
            .filter(ClientResponse::isSuccess)
            .map(clientResponse -> titular)
            .switchIfEmpty(Mono.empty()))
        .collect(Collectors.toSet());
  }

  private Mono<CurrentAccount> prepareCurrentAccount(CurrAccDTO dto, Client client) {
    Mono<Set<String>> validTitularsMono = validateAndFilterTitulars(dto.getAccountTitulars());

    return validTitularsMono.flatMap(validTitulars -> {
      if (validTitulars.isEmpty() && client.getClientType().equals("EMPRESARIAL")) {
        return Mono.error(new EmptyAttributes("At least one valid titular is required"));
      }

      dto.setAccountTitulars(validTitulars);
      CurrentAccount currentAccount = convertClass(dto);
      currentAccount.setAccountNumber(currentAccount.generateAccountNumber());

      if (client.getClientType().equals("EMPRESARIAL")) {
        currentAccount.setAccountTitulars(validTitulars);

        List<LegalSigner> legalSigners = dto.getLegalSigners()
            .stream()
            .map(item -> {
              LegalSigner signer = GenericMapper.mapToSpecificClass(item, LegalSigner.class);
              signer.setSignerNumber(signer.generateSignerNumber());
              return signer;
            })
            .collect(Collectors.toList());

        currentAccount.setAccountTitulars(validTitulars);
        currentAccount.setLegalSigners(legalSigners);
        currentAccount.setAccountNumber(currentAccount.generateAccountNumber());
      } else {
        currentAccount.setAccountTitulars(Collections.emptyNavigableSet());
        currentAccount.setLegalSigners(Collections.emptyList());
      }
      return Mono.just(currentAccount);
    });
  }

  @Override
  public Mono<Object> save(Object bankAccountDTO, Client client) {
    CurrAccDTO dto = customDTO(bankAccountDTO);
    Mono<Boolean> clientValidation = domainValidations.validateCurrentAccount(client, dto);

    return clientValidation.flatMap(isValid -> {
      if (Boolean.FALSE.equals(isValid)) {
        return Mono.error(new InvalidRule("The account you are creating breaks some rules"));
      }
      return prepareCurrentAccount(dto, client)
          .flatMap(currentRepo::insert)
          .map(this::customDTO);
    });
  }

  @Override
  public Mono<Object> update(Object foundAccount, Object updatedAccount) {
    CurrAccDTO currDTO = GenericMapper.mapToSpecificClass(updatedAccount, CurrAccDTO.class);
    return updateDomainValidations.validateAmmount(currDTO)
        .flatMap(validated -> {
          CurrentAccount foundCurrentAccount = GenericMapper.mapToSpecificClass(foundAccount, CurrentAccount.class);
          CurrAccDTO validatedAccount = GenericMapper.mapToSpecificClass(validated, CurrAccDTO.class);

          Query query = new Query(Criteria
              .where("accountNumber").is(foundCurrentAccount.getAccountNumber()));
          Update update = new Update()
              .set("balance", validatedAccount.getBalance())
              .set("transactions", validatedAccount.getTransactions());

          return reactiveMongoTemplate.findAndModify(query, update, CurrentAccount.class)
              .flatMap(account -> Mono.just("Account updated successfully"));
        });
  }
}

