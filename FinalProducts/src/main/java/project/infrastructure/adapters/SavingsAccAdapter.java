package project.infrastructure.adapters;

import org.springframework.stereotype.Repository;
import project.domain.model.Account.SavingsAccount;
import project.domain.ports.BAccountPort;
import project.infrastructure.adapters.MongoRepo.CurrAccRepo;
import project.infrastructure.adapters.MongoRepo.FxdTermRepo;
import project.infrastructure.adapters.MongoRepo.SavingsRepo;
import project.infrastructure.clientCalls.responses.Client;
import project.infrastructure.dto.account.BankAccountDTO;
import project.infrastructure.dto.account.SavingsDTO;
import project.infrastructure.exceptions.CustomException;
import project.infrastructure.mapper.GenericMapper;
import reactor.core.publisher.Mono;

@Repository
public class SavingsAccAdapter implements BAccountPort {
  private final SavingsRepo savingsrepo;
  private final CurrAccRepo curraccrepo;
  private final FxdTermRepo fxdtermrepo;

  public SavingsAccAdapter(SavingsRepo savingsrepo, CurrAccRepo curraccrepo, FxdTermRepo fxdtermrepo) {
    this.savingsrepo = savingsrepo;
    this.curraccrepo = curraccrepo;
    this.fxdtermrepo = fxdtermrepo;
  }

  private SavingsAccount convertClass(Object bankAccountDTO) {
    return GenericMapper.mapToSpecificClass(bankAccountDTO, SavingsAccount.class);
  }

  private BankAccountDTO customDTO(SavingsAccount savingsAccount) {
    return GenericMapper.mapToSpecificClass(savingsAccount, SavingsDTO.class);
  }

  public Mono<Boolean> isValidated(Client client) {
    Mono<Boolean> hasSavings = savingsrepo.existsByClientId(client.getCustomId());
    Mono<Boolean> hasCurrent = curraccrepo.existsByClientId(client.getCustomId());
    Mono<Boolean> hasFxdTerm = fxdtermrepo.existsByClientId(client.getCustomId());
    return hasSavings.or(hasCurrent).or(hasFxdTerm);
  }

  @Override
  public Mono<?> save(Object bankAccountDTO, Client client) {
    Mono<Boolean> isValid = isValidated(client);
    Mono<Boolean> isEnterprise = Mono.just(client.getClientType().equals("EMPRESARIAL"));

    return Mono.zip(isValid, isEnterprise)
            .flatMap(tuple -> {
              boolean notValid = tuple.getT1();
              boolean enterprise = tuple.getT2();

              if (notValid || enterprise) {
                return Mono.error(new CustomException("Can't create an account for this client"));
              }
              return Mono.just(bankAccountDTO)
                      .map(this::convertClass)
                      .flatMap(savingsrepo::insert)
                      .map(this::customDTO);
            });
  }
}
