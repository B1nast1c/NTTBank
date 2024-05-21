package project.transactionsservice.infrastructure.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project.transactionsservice.domain.strategies.TransactionsStrategies;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.exceptions.throwable.InvalidTransactionType;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class TransactionsFactory {
  private final Map<String, Function<TransactionDTO, Mono<Object>>> strategies = new HashMap<>();

  @Autowired
  public TransactionsFactory(TransactionsStrategies transactionsStrategies) {
    strategies.put("DEPOSITO", transactionsStrategies::depositAndWithdrawalStrategy);
    strategies.put("RETIRO", transactionsStrategies::depositAndWithdrawalStrategy);
    strategies.put("PAGO_CREDITO", transactionsStrategies::creditPaymentStrategy);
    strategies.put("CARGO_TARJETA", transactionsStrategies::creditCardChargeStrategy);
  }

  public Function<TransactionDTO, Mono<Object>> getStrategy(String transactionType) {
    Function<TransactionDTO, Mono<Object>> strategy = strategies.get(transactionType);
    if (strategy == null) {
      throw new InvalidTransactionType("Unsupported transaction type: " + transactionType);
    }
    return strategy;
  }
}
