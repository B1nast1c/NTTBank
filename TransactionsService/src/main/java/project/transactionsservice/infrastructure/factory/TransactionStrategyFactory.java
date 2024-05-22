package project.transactionsservice.infrastructure.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project.transactionsservice.domain.strategies.transactions.TransactionStrategy;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.exceptions.throwable.InvalidTransactionType;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class TransactionStrategyFactory {
  private final Map<String, TransactionStrategy> strategies;

  public TransactionStrategyFactory(Map<String, TransactionStrategy> strategies) {
    this.strategies = strategies;
  }
  
  public TransactionStrategy getStrategy(String transactionType) {
    return strategies.get(transactionType);
  }
}
