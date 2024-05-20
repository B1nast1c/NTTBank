package project.infrastructure.adapters;

import org.springframework.stereotype.Repository;
import project.domain.model.account.CurrentAccount;
import project.domain.model.account.FixedTermAccount;
import project.domain.model.account.SavingsAccount;
import project.infrastructure.adapters.mongorepo.CurrAccRepo;
import project.infrastructure.adapters.mongorepo.FxdTermRepo;
import project.infrastructure.adapters.mongorepo.SavingsRepo;
import project.infrastructure.dto.CurrAccDTO;
import project.infrastructure.dto.FxdTermDTO;
import project.infrastructure.dto.SavingsDTO;
import project.infrastructure.exceptions.throwable.NotFound;
import project.infrastructure.mapper.GenericMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class GenericAccAdapter {
  private final CurrAccRepo currAccRepository;
  private final FxdTermRepo fxdTermRepository;
  private final SavingsRepo savingsRepository;

  public GenericAccAdapter(CurrAccRepo currAccRepository, FxdTermRepo fxdTermRepository, SavingsRepo savingsRepository) {
    this.currAccRepository = currAccRepository;
    this.fxdTermRepository = fxdTermRepository;
    this.savingsRepository = savingsRepository;
  }

  public Flux<Object> findAll() {
    Flux<CurrAccDTO> currAccFlux = currAccRepository
        .findAll().map(account -> GenericMapper.mapToSpecificClass(account, CurrAccDTO.class));
    Flux<FxdTermDTO> fxdTermFlux = fxdTermRepository
        .findAll().map(account -> GenericMapper.mapToSpecificClass(account, FxdTermDTO.class));
    Flux<SavingsDTO> savingsFlux = savingsRepository
        .findAll().map(account -> GenericMapper.mapToSpecificClass(account, SavingsDTO.class));

    return Flux.concat(currAccFlux, fxdTermFlux, savingsFlux);
  }

  public Flux<Object> findByClientId(final String clientId) {
    Flux<CurrAccDTO> currAccFlux = currAccRepository.findAllByClientDocument(
        clientId).map(account -> GenericMapper.mapToSpecificClass(account, CurrAccDTO.class));
    Flux<FxdTermDTO> fxdTermFlux = fxdTermRepository.findAllByClientDocument(
        clientId).map(account -> GenericMapper.mapToSpecificClass(account, FxdTermDTO.class));
    Flux<SavingsDTO> savingsFlux = savingsRepository.findAllByClientDocument(
        clientId).map(account -> GenericMapper.mapToSpecificClass(account, SavingsDTO.class));

    return Flux.concat(currAccFlux, fxdTermFlux, savingsFlux);
  }

  public Mono<Object> findByAccountNumber(final String accountNumber) {
    Mono<CurrentAccount> currAccMono = currAccRepository.findByAccountNumber(accountNumber);
    Mono<FixedTermAccount> fxdTermMono = fxdTermRepository.findByAccountNumber(accountNumber);
    Mono<SavingsAccount> savingsMono = savingsRepository.findByAccountNumber(accountNumber);

    return Mono.firstWithValue(currAccMono, fxdTermMono, savingsMono)
        .map(object -> GenericMapper.mapToSpecificClass(object, Object.class))
        .switchIfEmpty(Mono.error(new NotFound("No account found with account number: " + accountNumber)));
  }
}
