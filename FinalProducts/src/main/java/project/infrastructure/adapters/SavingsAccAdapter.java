package project.infrastructure.adapters;

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

@Repository
public class SavingsAccAdapter implements BAccountPort {
  private final SavingsRepo savingsrepo;
  private final SaveDomainValidations saveDomainValidations;
  private final UpdateDomainValidations updateDomainValidations;
  private final ReactiveMongoTemplate reactiveMongoTemplate;

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

  @Override
  public Mono<Object> save(Object bankAccountDTO, Client client) {
    return saveDomainValidations
        .validateSavingsAccount(client)
        .flatMap(valid -> {
          if (Boolean.TRUE.equals(valid)) {
            return Mono.error(new InvalidRule("Can't create an account for this client"));
          } else {
            return Mono.just(bankAccountDTO)
                .map(this::convertClass)
                .flatMap(account -> {
                  account.setAccountNumber(account.generateAccountNumber());
                  return savingsrepo.insert(account);
                })
                .map(this::customDTO);
          }
        });
  }

  @Override
  public Mono<Object> update(Object foundAccount, Object updatedAccount) {
    SavingsDTO savingsDTO = GenericMapper.mapToSpecificClass(updatedAccount, SavingsDTO.class);
    SavingsAccount saveAccount = GenericMapper.mapToSpecificClass(foundAccount, SavingsAccount.class);

    return updateDomainValidations.validateSavingsAccount(savingsDTO, saveAccount)
        .flatMap(valid -> {
          if (Boolean.FALSE.equals(valid)) {
            return Mono.error(new InvalidRule("Transactions amount exceeds the limit"));
          }
          return updateDomainValidations.validateAmmount(updatedAccount)
              .flatMap(validated -> {
                SavingsDTO validatedAccount = GenericMapper.mapToSpecificClass(validated, SavingsDTO.class);

                Query query = new Query(Criteria.where("accountNumber").is(saveAccount.getAccountNumber()));
                Update update = new Update()
                    .set("balance", validatedAccount.getBalance())
                    .set("transactions", validatedAccount.getTransactions());

                return reactiveMongoTemplate.findAndModify(query, update, SavingsAccount.class)
                    .flatMap(account -> Mono.just("Account updated successfully"));
              });
        });
  }
}
