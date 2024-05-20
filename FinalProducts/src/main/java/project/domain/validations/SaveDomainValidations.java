package project.domain.validations;

import org.springframework.stereotype.Component;
import project.infrastructure.adapters.mongorepo.CurrAccRepo;
import project.infrastructure.adapters.mongorepo.FxdTermRepo;
import project.infrastructure.adapters.mongorepo.SavingsRepo;
import project.infrastructure.clientcalls.responses.Client;
import project.infrastructure.dto.CurrAccDTO;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class SaveDomainValidations {
  private static final String EMPRESARIAL = "EMPRESARIAL";
  private final CurrAccRepo currentRepo;
  private final SavingsRepo savingsrepo;
  private final FxdTermRepo fxdtermrepo;

  public SaveDomainValidations(CurrAccRepo currentRepo, SavingsRepo savingsrepo, FxdTermRepo fxdtermrepo) {
    this.currentRepo = currentRepo;
    this.savingsrepo = savingsrepo;
    this.fxdtermrepo = fxdtermrepo;
  }

  private Mono<Boolean> hasSavingsAccount(Client client) {
    return savingsrepo.existsByClientDocument(client.getDocumentNumber());
  }

  private Mono<Boolean> hasCurrentAccount(Client client) {
    return currentRepo.existsByClientDocument(client.getDocumentNumber());
  }

  private Mono<Boolean> hasFxdTermAccount(Client client) {
    return fxdtermrepo.existsByClientDocument(client.getDocumentNumber());
  }

  public Mono<Boolean> validateCurrentAccount(Client client, CurrAccDTO currAccount) {
    Mono<Boolean> isEnterpriseValidated = Mono.just(
        Objects.equals(client.getClientType(), EMPRESARIAL)
            && !currAccount.getAccountTitulars().isEmpty());

    return Mono.zip(hasSavingsAccount(client),
            hasCurrentAccount(client),
            hasFxdTermAccount(client),
            isEnterpriseValidated)
        .map(tuple -> {
          boolean hasAnyAccount = tuple.getT1() || tuple.getT2() || tuple.getT3();
          boolean enterpriseValid = tuple.getT4();
          return !hasAnyAccount || enterpriseValid;
        });
  }

  public Mono<Boolean> validateSavingsAccount(Client client) {
    return Mono.zip(hasSavingsAccount(client),
            hasCurrentAccount(client),
            hasFxdTermAccount(client))
        .map(tuple -> {
          boolean hasAnyAccount = tuple.getT1() || tuple.getT2() || tuple.getT3();
          return hasAnyAccount || Objects.equals(client.getClientType(), EMPRESARIAL);
        });
  }

  public Mono<Boolean> validateFxdTermAccount(Client client) {
    return Mono.zip(hasSavingsAccount(client),
            hasCurrentAccount(client),
            hasFxdTermAccount(client))
        .map(tuple -> {
          boolean hasAnyAccount = tuple.getT1() || tuple.getT2();
          return hasAnyAccount || Objects.equals(client.getClientType(), EMPRESARIAL);
        });
  }
}
