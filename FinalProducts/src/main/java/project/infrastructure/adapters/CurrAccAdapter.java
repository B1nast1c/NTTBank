package project.infrastructure.adapters;

import lombok.extern.slf4j.Slf4j;
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

/**
 * Adaptador para cuentas corrientes.
 */
@Slf4j
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

  /**
   * Valida y filtra los titulares VÁLIDOS de la cuenta corriente.
   *
   * @param titulars Titulares de la cuenta corriente
   * @return Mono que representa el conjunto de titulares válidos
   */
  private Mono<Set<String>> validateAndFilterTitulars(Set<String> titulars) {
    return Flux.fromIterable(titulars)
        .flatMap(titular -> clientService.getClientByiD(titular)
            .filter(ClientResponse::isSuccess)
            .map(clientResponse -> titular)
            .switchIfEmpty(Mono.empty()))
        .collect(Collectors.toSet());
  }

  /**
   * Prepara una cuenta corriente para ser guardada.
   *
   * @param dto    DTO de la cuenta corriente
   * @param client Cliente asociado a la cuenta
   * @return Mono que representa la cuenta corriente preparada
   */
  private Mono<CurrentAccount> prepareCurrentAccount(CurrAccDTO dto, Client client) {
    Mono<Set<String>> validTitularsMono = validateAndFilterTitulars(dto.getAccountTitulars());

    return validTitularsMono.flatMap(validTitulars -> {
      if (validTitulars.isEmpty() && client.getClientType().equals("EMPRESARIAL")) {
        log.warn("The account titular list is empty");
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
            .collect(Collectors.toList()); // Lista de firmates legales

        currentAccount.setAccountTitulars(validTitulars);
        currentAccount.setLegalSigners(legalSigners);
        currentAccount.setAccountNumber(currentAccount.generateAccountNumber());
      } else {
        currentAccount.setAccountTitulars(Collections.emptyNavigableSet());
        currentAccount.setLegalSigners(Collections.emptyList());
      }
      return Mono.just(currentAccount); // Elemento final a insertar
    });
  }

  /**
   * Guarda una nueva cuenta corriente.
   *
   * @param bankAccountDTO DTO de la cuenta corriente
   * @param client         Cliente asociado a la cuenta
   * @return Mono que representa la cuenta corriente guardada
   */
  @Override
  public Mono<Object> save(Object bankAccountDTO, Client client) {
    CurrAccDTO dto = customDTO(bankAccountDTO);
    Mono<Boolean> clientValidation = domainValidations.validateCurrentAccount(client, dto);

    return clientValidation.flatMap(isValid -> {
      if (Boolean.FALSE.equals(isValid)) {
        log.warn("The account creation breaks some domain rules");
        return Mono.error(new InvalidRule("The account you are creating breaks some rules"));
      }
      log.info("Account for the client -> {} has been created", client.getDocumentNumber());
      return prepareCurrentAccount(dto, client) // Prepara la cuenta corriente para ser guardada
          .flatMap(currentRepo::insert)
          .map(this::customDTO);
    });
  }

  /**
   * Actualiza una cuenta corriente existente.
   *
   * @param foundAccount   Cuenta corriente encontrada
   * @param updatedAccount DTO actualizado de la cuenta corriente
   * @return Mono que indica el resultado de la actualización
   */
  @Override
  public Mono<Object> update(Object foundAccount, Object updatedAccount) {
    CurrAccDTO currDTO = GenericMapper.mapToSpecificClass(updatedAccount, CurrAccDTO.class);
    return updateDomainValidations.validateAmmount(currDTO)
        .flatMap(validated -> {
          // Cuenta a editar, y elemento que contiene las futuras ediciones - EN ESE ORDEN
          CurrentAccount foundCurrentAccount = GenericMapper.mapToSpecificClass(foundAccount, CurrentAccount.class);
          CurrAccDTO validatedAccount = GenericMapper.mapToSpecificClass(validated, CurrAccDTO.class);

          Query query = new Query(Criteria
              .where("accountNumber").is(foundCurrentAccount.getAccountNumber()));
          Update update = new Update()
              .set("balance", validatedAccount.getBalance())
              .set("transactions", validatedAccount.getTransactions());

          log.info("Account -> {} has been updated", foundCurrentAccount.getAccountNumber());
          return reactiveMongoTemplate.findAndModify(query, update, CurrentAccount.class)
              .flatMap(account -> Mono.just("Account updated successfully"));
        });
  }
}