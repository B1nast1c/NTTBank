package project.domain.validations;

import org.springframework.stereotype.Component;
import project.infrastructure.adapters.mongorepo.CurrAccRepo;
import project.infrastructure.adapters.mongorepo.FxdTermRepo;
import project.infrastructure.adapters.mongorepo.SavingsRepo;
import project.infrastructure.clientcalls.responses.Client;
import project.infrastructure.dto.CurrAccDTO;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * La clase SaveDomainValidations contiene métodos para validar la creación de diferentes tipos de cuentas.
 */
@Component
public class SaveDomainValidations {
  private static final String EMPRESARIAL = "EMPRESARIAL";
  private final CurrAccRepo currentRepo;
  private final SavingsRepo savingsRepo;
  private final FxdTermRepo fxdTermRepo;

  /**
   * Constructor para SaveDomainValidations.
   *
   * @param currentRepo Repositorio de cuentas corrientes.
   * @param savingsRepo Repositorio de cuentas de ahorro.
   * @param fxdTermRepo Repositorio de cuentas a plazo fijo.
   */
  public SaveDomainValidations(
      CurrAccRepo currentRepo,
      SavingsRepo savingsRepo,
      FxdTermRepo fxdTermRepo) {
    this.currentRepo = currentRepo;
    this.savingsRepo = savingsRepo;
    this.fxdTermRepo = fxdTermRepo;
  }

  /**
   * Método para validar la creación de una cuenta corriente.
   *
   * @param client      Cliente asociado a la cuenta.
   * @param currAccount DTO de la cuenta corriente.
   * @return Un Mono que indica si la validación fue exitosa.
   */
  public Mono<Boolean> validateCurrentAccount(Client client, CurrAccDTO currAccount) {
    Mono<Boolean> isEnterpriseValidated = Mono.just(
        Objects.equals(client.getClientType(), EMPRESARIAL)
            && !currAccount.getAccountTitulars().isEmpty());

    return Mono.zip(hasSavingsAccount(client),
            hasCurrentAccount(client),
            hasFxdTermAccount(client),
            isEnterpriseValidated)
        .map(tuple -> { // Almacenamiento de un tercio de booleanos, que determinan la creación o no de una cuenta
          boolean hasAnyAccount = tuple.getT1() || tuple.getT2() || tuple.getT3();
          boolean enterpriseValid = tuple.getT4();
          return !hasAnyAccount || enterpriseValid;
        });
  }

  /**
   * Método para validar la creación de una cuenta de ahorros.
   *
   * @param client Cliente asociado a la cuenta.
   * @return Un Mono que indica si la validación fue exitosa.
   */
  public Mono<Boolean> validateSavingsAccount(Client client) {
    return Mono.zip(hasSavingsAccount(client),
            hasCurrentAccount(client),
            hasFxdTermAccount(client))
        .map(tuple -> {
          boolean hasAnyAccount = tuple.getT1() || tuple.getT2() || tuple.getT3();
          return hasAnyAccount || Objects.equals(client.getClientType(), EMPRESARIAL);
        });
  }

  /**
   * Método para validar la creación de una cuenta a plazo fijo.
   *
   * @param client Cliente asociado a la cuenta.
   * @return Un Mono que indica si la validación fue exitosa.
   */
  public Mono<Boolean> validateFxdTermAccount(Client client) {
    return Mono.zip(hasSavingsAccount(client),
            hasCurrentAccount(client),
            hasFxdTermAccount(client))
        .map(tuple -> {
          boolean hasAnyAccount = tuple.getT1() || tuple.getT2();
          return hasAnyAccount || Objects.equals(client.getClientType(), EMPRESARIAL);
        });
  }

  // Métodos privados para verificar si el cliente ya tiene cuentas de diferentes tipos.

  private Mono<Boolean> hasSavingsAccount(Client client) {
    return savingsRepo.existsByClientDocument(client.getDocumentNumber());
  }

  private Mono<Boolean> hasCurrentAccount(Client client) {
    return currentRepo.existsByClientDocument(client.getDocumentNumber());
  }

  private Mono<Boolean> hasFxdTermAccount(Client client) {
    return fxdTermRepo.existsByClientDocument(client.getDocumentNumber());
  }
}