package project.infrastructure.adapters;

import org.springframework.stereotype.Repository;
import project.domain.model.Account.FixedTermAccount;
import project.domain.ports.BAccountPort;
import project.infrastructure.adapters.MongoRepo.CurrAccRepo;
import project.infrastructure.adapters.MongoRepo.FxdTermRepo;
import project.infrastructure.adapters.MongoRepo.SavingsRepo;
import project.infrastructure.clientCalls.responses.Client;
import project.infrastructure.dto.account.FxdTermDTO;
import project.infrastructure.exceptions.CustomException;
import project.infrastructure.mapper.GenericMapper;
import reactor.core.publisher.Mono;

@Repository
public class FxdTermAdapter implements BAccountPort {
  private final CurrAccRepo currentRepo;
  private final SavingsRepo savingsrepo;
  private final FxdTermRepo fxdTermRepo;

  public FxdTermAdapter(CurrAccRepo currentRepo, SavingsRepo savingsrepo, FxdTermRepo fxdTermRepo) {
    this.currentRepo = currentRepo;
    this.savingsrepo = savingsrepo;
    this.fxdTermRepo = fxdTermRepo;
  }

  private FixedTermAccount convertClass(Object bankAccountDTO) {
    return GenericMapper.mapToSpecificClass(bankAccountDTO, FixedTermAccount.class);
  }

  private FxdTermDTO customDTO(FixedTermAccount fxdAccount) {
    return GenericMapper.mapToSpecificClass(fxdAccount, FxdTermDTO.class);
  }

  public Mono<Boolean> isPersonalValidated(Client client) {
    Mono<Boolean> hasSavings = savingsrepo.existsByClientId(client.getCustomId());
    Mono<Boolean> hasCurrent = currentRepo.existsByClientId(client.getCustomId());
    return hasSavings.or(hasCurrent).or(hasCurrent);
  }

  public Mono<?> save(Object bankAccountDTO, Client client) {

    Mono<Boolean> isEnterprise = Mono.just(client.getClientType().equals("EMPRESARIAL"));
    Mono<Boolean> isValid = isPersonalValidated(client);

    return Mono.zip(isValid, isEnterprise).flatMap(tuple -> {
      boolean notValid = tuple.getT1();
      boolean enterprise = tuple.getT2();
      if (notValid || enterprise) {
        return Mono.error(new CustomException("Can't create an account for this client"));
      } else {
        return Mono.just(bankAccountDTO).map(this::convertClass).flatMap(fxdTermRepo::insert).map(this::customDTO);
      }
    });
  }
}
