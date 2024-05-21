package project.transactionsservice.infrastructure.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project.transactionsservice.domain.strategies.AccountStrategies;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.servicecalls.responses.AccountResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@Component
public class AccountsFactory {
  private final Map<String, BiFunction<TransactionDTO, AccountResponse, Mono<TransactionDTO>>> strategies = new HashMap<>();

  @Autowired
  public AccountsFactory(AccountStrategies accountStrategies) {
    strategies.put("PLAZO_FIJO", accountStrategies::savingsAndFxdStrategy);
    strategies.put("AHORRO", accountStrategies::savingsAndFxdStrategy);
    strategies.put("CUENTA_CORRIENTE", accountStrategies::currAccStrategy);
  }

  public BiFunction<TransactionDTO, AccountResponse, Mono<TransactionDTO>> getStrategy(String accountType) {
    return strategies.get(accountType);
  }
}
