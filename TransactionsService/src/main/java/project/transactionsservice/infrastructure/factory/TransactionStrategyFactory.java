package project.transactionsservice.infrastructure.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.exceptions.throwable.InvalidTransaction;
import project.transactionsservice.infrastructure.strategies.transactions.TransactionStrategy;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class TransactionStrategyFactory {
  private final Map<String, Function<TransactionDTO, Mono<Object>>> strategies = new HashMap<>();

  @Autowired
  public TransactionStrategyFactory(
      @Qualifier("depositStrategy") TransactionStrategy depositStrategy,
      @Qualifier("withdrawalStrategy") TransactionStrategy withdrawalStrategy) {

    for (TransactionStrategy strategy : List.of(depositStrategy, withdrawalStrategy)) {
      switch (strategy.getClass().getSimpleName()) {
        case "DepositStrategy":
          strategies.put("DEPOSITO", strategy::execute);
          break;
        case "WithdrawalStrategy":
          strategies.put("RETIRO", strategy::execute);
          break;
        // Añade otros casos aquí para otras estrategias (TIPO DE TRANSACCIONES)
        default:
          throw new InvalidTransaction("Transaction type not supported");
      }
    }
  }

  public Function<TransactionDTO, Mono<Object>> getStrategy(String transactionType) {
    return strategies.get(transactionType);
  }
}

