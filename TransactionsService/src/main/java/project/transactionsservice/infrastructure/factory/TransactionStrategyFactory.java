package project.transactionsservice.infrastructure.factory;

import org.springframework.stereotype.Component;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.strategies.TransactionStrategy;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class TransactionStrategyFactory {
  private final Map<String, Function<TransactionDTO, Mono<Object>>> strategies = new HashMap<>();

  // Aumentas más estrategias
  public TransactionStrategyFactory(TransactionStrategy transactionStrategy) {
    strategies.put("DEPOSITO", transactionStrategy::depositStrategy);
    strategies.put("RETIRO", transactionStrategy::withdrawalStrategy);
  }

  public Function<TransactionDTO, Mono<Object>> getStrategy(String accountType) {
    return strategies.get(accountType);
  }
}

