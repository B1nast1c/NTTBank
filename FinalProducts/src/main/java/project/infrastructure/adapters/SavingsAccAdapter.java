package project.infrastructure.adapters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import project.domain.model.account.SavingsAccount;
import project.domain.ports.BAccountPort;
import project.domain.validations.SaveDomainValidations;
import project.domain.validations.UpdateDomainValidations;
import project.infrastructure.adapters.mongorepo.SavingsRepo;
import project.infrastructure.clientcalls.responses.Client;
import project.infrastructure.dto.BankAccountDTO;
import project.infrastructure.dto.SavingsDTO;
import project.infrastructure.exceptions.throwable.InvalidRule;
import project.infrastructure.mapper.GenericMapper;
import reactor.core.publisher.Mono;

/**
 * Adaptador para manejar operaciones CRUD en cuentas de ahorro.
 */
@Slf4j
@Repository
public class SavingsAccAdapter implements BAccountPort {
  private final SavingsRepo savingsrepo;
  private final SaveDomainValidations saveDomainValidations;
  private final UpdateDomainValidations updateDomainValidations;
  private final ReactiveMongoTemplate reactiveMongoTemplate;

  /**
   * Constructor para inicializar el adaptador de cuenta de ahorro.
   *
   * @param savingsrepo             Repositorio de cuentas de ahorro.
   * @param saveDomainValidations   Validaciones de dominio para guardar.
   * @param updateDomainValidations Validaciones de dominio para actualizar.
   * @param reactiveMongoTemplate   Plantilla reactiva para MongoDB.
   */
  public SavingsAccAdapter(SavingsRepo savingsrepo,
                           SaveDomainValidations saveDomainValidations,
                           UpdateDomainValidations updateDomainValidations,
                           ReactiveMongoTemplate reactiveMongoTemplate) {
    this.savingsrepo = savingsrepo;
    this.saveDomainValidations = saveDomainValidations;
    this.updateDomainValidations = updateDomainValidations;
    this.reactiveMongoTemplate = reactiveMongoTemplate;
  }

  private SavingsAccount convertClass(Object bankAccountDTO) {
    return GenericMapper.mapToSpecificClass(bankAccountDTO, SavingsAccount.class);
  }

  private BankAccountDTO customDTO(SavingsAccount savingsAccount) {
    return GenericMapper.mapToSpecificClass(savingsAccount, SavingsDTO.class);
  }

  /**
   * Guarda una nueva cuenta de ahorro en la base de datos.
   *
   * @param bankAccountDTO Datos de la cuenta de ahorro a guardar.
   * @param client         Cliente asociado a la cuenta de ahorro.
   * @return Un mono que representa el resultado de la operación de guardado.
   */
  @Override
  public Mono<Object> save(Object bankAccountDTO, Client client) {
    return saveDomainValidations
        .validateSavingsAccount(client) // Restricciones específicas de la cuenta
        .flatMap(valid -> { // Existencia de otros tipos de cuenta
          if (Boolean.TRUE.equals(valid)) {
            log.warn("The account creation breaks some domain rules");
            return Mono.error(new InvalidRule("Can't create an account for this client"));
          } else {
            return Mono.just(bankAccountDTO)
                .map(this::convertClass) // Clase objetivo
                .flatMap(account -> {
                  account.setAccountNumber(account.generateAccountNumber());
                  return savingsrepo.insert(account);
                })
                .map(this::customDTO); // DTO del elemento insertado
          }
        });
  }

  @Override
  public Mono<Object> update(Object foundAccount, Object updatedAccount) {
    SavingsDTO savingsDTO = GenericMapper
        .mapToSpecificClass(updatedAccount, SavingsDTO.class);
    SavingsAccount saveAccount = GenericMapper
        .mapToSpecificClass(foundAccount, SavingsAccount.class);

    return updateDomainValidations
        .validateSavingsAccount(savingsDTO, saveAccount) // Restricciones específicas de la cuenta
        .flatMap(valid -> {
          if (Boolean.FALSE.equals(valid)) {
            log.warn("The saving account validation has failed");
            return Mono.error(new InvalidRule("Transactions ammount exceeds the limit"));
          }
          return updateDomainValidations.validateAmmount(updatedAccount)
              .flatMap(validated -> {
                SavingsDTO validatedAccount = GenericMapper.mapToSpecificClass(validated, SavingsDTO.class);

                Query query = new Query(Criteria.where("accountNumber")
                    .is(saveAccount.getAccountNumber())); // Actualización de balance y transactions
                Update update = new Update()
                    .set("balance", validatedAccount.getBalance())
                    .set("transactions", validatedAccount.getTransactions());

                log.info("Account -> {} has been updated", validatedAccount.getAccountNumber());
                return reactiveMongoTemplate.findAndModify(query, update, SavingsAccount.class)
                    .flatMap(account -> Mono.just("Account updated successfully"));
              });
        });
  }
}
