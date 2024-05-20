package project.infrastructure.adapters;

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

@Repository
public class FxdTermAdapter implements BAccountPort {
  private final FxdTermRepo fxdTermRepo;
  private final SaveDomainValidations saveDomainValidations;
  private final UpdateDomainValidations updateDomainValidations;
  private final ReactiveMongoTemplate reactiveMongoTemplate;

  public FxdTermAdapter(FxdTermRepo fxdTermRepo, SaveDomainValidations saveDomainValidations, UpdateDomainValidations updateDomainValidations, ReactiveMongoTemplate reactiveMongoTemplate) {
    this.fxdTermRepo = fxdTermRepo;
    this.saveDomainValidations = saveDomainValidations;
    this.updateDomainValidations = updateDomainValidations;
    this.reactiveMongoTemplate = reactiveMongoTemplate;
  }

  private FixedTermAccount convertClass(Object bankAccountDTO) {
    return GenericMapper.mapToSpecificClass(bankAccountDTO, FixedTermAccount.class);
  }

  private FxdTermDTO customDTO(FixedTermAccount fxdAccount) {
    return GenericMapper.mapToSpecificClass(fxdAccount, FxdTermDTO.class);
  }

  @Override
  public Mono<Object> save(Object bankAccountDTO, Client client) {
    return saveDomainValidations.validateFxdTermAccount(client).flatMap(isValid -> {
      if (Boolean.TRUE.equals(isValid)) {
        return Mono.error(new InvalidRule("Can't create an account for this client"));
      } else {
        return Mono.just(bankAccountDTO)
            .map(this::convertClass)
            .flatMap(account -> {
              account.setAccountNumber(account.generateAccountNumber());
              return fxdTermRepo.insert(account);
            })
            .map(this::customDTO);
      }
    });
  }

  @Override
  public Mono<Object> update(Object foundAccount, Object updatedAccount) {
    FxdTermDTO fxdtermDTO = GenericMapper.mapToSpecificClass(updatedAccount, FxdTermDTO.class);
    return updateDomainValidations.validateAmmount(fxdtermDTO)
        .flatMap(validated -> {
          FixedTermAccount foundFxdAccount = GenericMapper.mapToSpecificClass(foundAccount, FixedTermAccount.class);
          FxdTermDTO validatedAccount = GenericMapper.mapToSpecificClass(validated, FxdTermDTO.class);

          Query query = new Query(Criteria
              .where("accountNumber").is(foundFxdAccount.getAccountNumber()));
          Update update = new Update()
              .set("balance", validatedAccount.getBalance())
              .set("transactions", validatedAccount.getTransactions());

          return reactiveMongoTemplate.findAndModify(query, update, FixedTermAccount.class)
              .flatMap(account -> Mono.just("Account updated successfully"));
        });
  }
}
