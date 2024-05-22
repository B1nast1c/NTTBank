package project.infrastructure.adapters;

import lombok.extern.slf4j.Slf4j;
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

/**
 * Adaptador para interactuar con cuentas genéricas en la base de datos.
 */
@Slf4j
@Repository
public class GenericAccAdapter {
  private final CurrAccRepo currAccRepository;
  private final FxdTermRepo fxdTermRepository;
  private final SavingsRepo savingsRepository;

  /**
   * Inicializa un nuevo adaptador de cuentas genéricas.
   *
   * @param currAccRepository Repositorio de cuentas corrientes
   * @param fxdTermRepository Repositorio de cuentas de plazo fijo
   * @param savingsRepository Repositorio de cuentas de ahorro
   */
  public GenericAccAdapter(CurrAccRepo currAccRepository, FxdTermRepo fxdTermRepository, SavingsRepo savingsRepository) {
    this.currAccRepository = currAccRepository;
    this.fxdTermRepository = fxdTermRepository;
    this.savingsRepository = savingsRepository;
  }

  /**
   * Recupera todas las cuentas disponibles en la base de datos.
   *
   * @return Un flujo (secuencia) de objetos que representan cuentas genéricas.
   */
  public Flux<Object> findAll() {
    Flux<CurrAccDTO> currAccFlux = currAccRepository
        .findAll().map(account -> GenericMapper.mapToSpecificClass(account, CurrAccDTO.class));
    Flux<FxdTermDTO> fxdTermFlux = fxdTermRepository
        .findAll().map(account -> GenericMapper.mapToSpecificClass(account, FxdTermDTO.class));
    Flux<SavingsDTO> savingsFlux = savingsRepository
        .findAll().map(account -> GenericMapper.mapToSpecificClass(account, SavingsDTO.class));

    log.info("Retrieving all accounts");
    return Flux.concat(currAccFlux, fxdTermFlux, savingsFlux);
  }

  /**
   * Recupera todas las cuentas asociadas a un cliente específico.
   *
   * @param clientId ID del cliente
   * @return Un flujo (secuencia) de objetos que representan cuentas genéricas asociadas al cliente.
   */
  public Flux<Object> findByClientId(final String clientId) {
    Flux<CurrAccDTO> currAccFlux = currAccRepository.findAllByClientDocument(
        clientId).map(account -> GenericMapper.mapToSpecificClass(account, CurrAccDTO.class));
    Flux<FxdTermDTO> fxdTermFlux = fxdTermRepository.findAllByClientDocument(
        clientId).map(account -> GenericMapper.mapToSpecificClass(account, FxdTermDTO.class));
    Flux<SavingsDTO> savingsFlux = savingsRepository.findAllByClientDocument(
        clientId).map(account -> GenericMapper.mapToSpecificClass(account, SavingsDTO.class));

    log.info("Retrieving all accounts for the client {}", clientId);
    return Flux.concat(currAccFlux, fxdTermFlux, savingsFlux);
  }

  /**
   * Recupera una cuenta específica mediante su número de cuenta.
   *
   * @param accountNumber Número de cuenta
   * @return Un mono (objeto) que representa la cuenta específica.
   * @throws NotFound Si no se encuentra ninguna cuenta con el número especificado.
   */
  public Mono<Object> findByAccountNumber(final String accountNumber) {
    Mono<CurrentAccount> currAccMono = currAccRepository.findByAccountNumber(accountNumber);
    Mono<FixedTermAccount> fxdTermMono = fxdTermRepository.findByAccountNumber(accountNumber);
    Mono<SavingsAccount> savingsMono = savingsRepository.findByAccountNumber(accountNumber);

    log.info("Retrieving the account -> {}", accountNumber);
    return Mono.firstWithValue(currAccMono, fxdTermMono, savingsMono) // Primer elemento NO VACÍO
        .map(object -> GenericMapper.mapToSpecificClass(object, Object.class))
        .switchIfEmpty(Mono.error(new NotFound("No account found with account number: " + accountNumber)));
  }
}