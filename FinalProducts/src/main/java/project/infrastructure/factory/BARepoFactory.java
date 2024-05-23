package project.infrastructure.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.domain.ports.BAccountPort;
import project.infrastructure.adapters.CurrAccAdapter;
import project.infrastructure.adapters.FxdTermAdapter;
import project.infrastructure.adapters.SavingsAccAdapter;
import project.infrastructure.exceptions.throwable.WrongAccountType;

/**
 * La clase BARepoFactory es una fábrica que proporciona adaptadores de
 * repositorio de cuentas bancarias.
 */
@Slf4j
@Component
public class BARepoFactory {
  private final SavingsAccAdapter savingsAccAdapter;
  private final FxdTermAdapter fxdTermAdapter;
  private final CurrAccAdapter currAccAdapter;

  /**
   * Constructor de la clase BARepoFactory.
   *
   * @param savingsAccAdapter El adaptador de repositorio de cuentas de ahorro.
   * @param fxdTermAdapter    El adaptador de repositorio de cuentas a plazo fijo.
   * @param currAccAdapter    El adaptador de repositorio de cuentas corrientes.
   */
  public BARepoFactory(
      SavingsAccAdapter savingsAccAdapter,
      FxdTermAdapter fxdTermAdapter,
      CurrAccAdapter currAccAdapter) {
    this.savingsAccAdapter = savingsAccAdapter;
    this.fxdTermAdapter = fxdTermAdapter;
    this.currAccAdapter = currAccAdapter;
  }

  /**
   * Método para obtener el adaptador de repositorio adecuado según el tipo de cuenta especificado.
   *
   * @param type El tipo de cuenta.
   * @return El adaptador de repositorio correspondiente al tipo de cuenta.
   * @throws WrongAccountType Si se proporciona un tipo de cuenta no compatible.
   */
  public BAccountPort getAdapter(String type) {
    log.info("Assigning adapter to the incoming account");
    switch (type) {
      case "AHORRO":
        return savingsAccAdapter;
      case "CUENTA_CORRIENTE":
        return currAccAdapter;
      case "PLAZO_FIJO":
        return fxdTermAdapter;
      default:
        log.warn("Account type not allowed");
        throw new WrongAccountType("Account type not allowed : " + type);
    }
  }
}