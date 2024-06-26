package project.infrastructure.adapters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import project.domain.model.account.FixedTermAccount;
import project.domain.ports.BAccountPort;
import project.domain.validations.SaveDomainValidations;
import project.domain.validations.UpdateDomainValidations;
import project.infrastructure.adapters.mongorepo.FxdTermRepo;
import project.infrastructure.clientcalls.responses.Client;
import project.infrastructure.dto.FxdTermDTO;
import project.infrastructure.exceptions.throwable.InvalidRule;
import project.infrastructure.mapper.GenericMapper;
import reactor.core.publisher.Mono;

/**
 * Adaptador para cuentas a plazo fijo.
 */
@Slf4j
@Repository
public class FxdTermAdapter implements BAccountPort {
  private final FxdTermRepo fxdTermRepo;
  private final SaveDomainValidations saveDomainValidations;
  private final UpdateDomainValidations updateDomainValidations;
  private final ReactiveMongoTemplate reactiveMongoTemplate;

  /**
   * Inicializa un nuevo adaptador de cuentas a plazo fijo.
   *
   * @param fxdTermRepo             Repositorio de cuentas a plazo fijo
   * @param saveDomainValidations   Validaciones para guardar cuentas
   * @param updateDomainValidations Validaciones para actualizar cuentas
   * @param reactiveMongoTemplate   Plantilla reactiva de MongoDB
   */
  public FxdTermAdapter(FxdTermRepo fxdTermRepo,
                        SaveDomainValidations saveDomainValidations,
                        UpdateDomainValidations updateDomainValidations,
                        ReactiveMongoTemplate reactiveMongoTemplate) {
    this.fxdTermRepo = fxdTermRepo;
    this.saveDomainValidations = saveDomainValidations;
    this.updateDomainValidations = updateDomainValidations;
    this.reactiveMongoTemplate = reactiveMongoTemplate;
  }

  /**
   * Convierte un objeto DTO en una cuenta a plazo fijo.
   *
   * @param bankAccountDTO DTO de la cuenta
   * @return Cuenta a plazo fijo
   */
  private FixedTermAccount convertClass(Object bankAccountDTO) {
    return GenericMapper.mapToSpecificClass(bankAccountDTO, FixedTermAccount.class);
  }

  /**
   * Convierte una cuenta a plazo fijo en un objeto DTO personalizado.
   *
   * @param fxdAccount Cuenta a plazo fijo
   * @return DTO personalizado de la cuenta
   */
  private FxdTermDTO customDTO(FixedTermAccount fxdAccount) {
    return GenericMapper.mapToSpecificClass(fxdAccount, FxdTermDTO.class);
  }

  /**
   * Guarda una cuenta a plazo fijo.
   *
   * @param bankAccountDTO DTO de la cuenta a guardar
   * @param client         Cliente asociado a la cuenta
   * @return Mono que representa la cuenta guardada
   */
  @Override
  public Mono<Object> save(Object bankAccountDTO, Client client) {
    return saveDomainValidations.
        validateFxdTermAccount(client)
        .flatMap(isValid -> {
          if (Boolean.TRUE.equals(isValid)) {
            log.warn("The account creation breaks some domain rules");
            return Mono.error(new InvalidRule("Can't create an account for this client"));
          } else {
            return Mono.just(bankAccountDTO)
                .map(this::convertClass)
                .flatMap(account -> {
                  account.setAccountNumber(account.generateAccountNumber());
                  log.info("Account -> {} has been created", account.getAccountNumber());
                  return fxdTermRepo.insert(account);
                })
                .map(this::customDTO);
          }
        });
  }

  /**
   * Actualiza una cuenta a plazo fijo.
   *
   * @param foundAccount   Cuenta a plazo fijo encontrada
   * @param updatedAccount Cuenta a plazo fijo actualizada
   * @return Mono que indica el resultado de la actualización
   */
  @Override
  public Mono<Object> update(Object foundAccount, Object updatedAccount) {
    FxdTermDTO fxdtermDTO = GenericMapper.mapToSpecificClass(updatedAccount, FxdTermDTO.class);
    return updateDomainValidations.validateAmmount(fxdtermDTO)
        .flatMap(validated -> {
          FixedTermAccount foundFxdAccount = GenericMapper
              .mapToSpecificClass(foundAccount, FixedTermAccount.class);
          FxdTermDTO validatedAccount = GenericMapper
              .mapToSpecificClass(validated, FxdTermDTO.class);

          Query query = new Query(Criteria
              .where("accountNumber").is(foundFxdAccount.getAccountNumber()));
          Update update = new Update()
              .set("balance", validatedAccount.getBalance())
              .set("transactions", validatedAccount.getTransactions());

          log.info("Account -> {} has been updated", foundFxdAccount.getAccountNumber());
          return reactiveMongoTemplate.findAndModify(query, update, FixedTermAccount.class)
              .flatMap(account -> Mono.just("Account updated successfully"));
        });
  }
}