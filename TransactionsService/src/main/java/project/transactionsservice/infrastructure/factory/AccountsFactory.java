package project.transactionsservice.infrastructure.factory;

import org.springframework.stereotype.Component;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.servicecalls.responses.AccountResponse;
import project.transactionsservice.infrastructure.strategies.AccountStrategies;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Fábrica de estrategias de cuentas que selecciona la estrategia adecuada según el tipo de cuenta.
 */
@Component
public class AccountsFactory {
  private final Map<String, BiFunction<TransactionDTO, AccountResponse, Mono<TransactionDTO>>> strategies = new HashMap<>();

  /**
   * Constructor de AccountsFactory.
   *
   * @param accountStrategies estrategias disponibles para las cuentas
   */
  public AccountsFactory(AccountStrategies accountStrategies) {
    strategies.put("PLAZO_FIJO", accountStrategies::fxdStrategy);
    strategies.put("AHORRO", accountStrategies::savingsStrategy);
    strategies.put("CUENTA_CORRIENTE", accountStrategies::currAccStrategy);
  }

  /**
   * Obtiene la estrategia correspondiente para el tipo de cuenta proporcionado.
   *
   * @param accountType el tipo de cuenta
   * @return la estrategia correspondiente como una función BiFunction
   */
  public BiFunction<TransactionDTO, AccountResponse, Mono<TransactionDTO>> getStrategy(String accountType) {
    return strategies.get(accountType);
  }
}