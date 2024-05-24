package project.transactionsservice.infrastructure.factory;

import org.springframework.stereotype.Component;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.strategies.TransactionStrategy;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Fábrica de estrategias de transacciones que selecciona la estrategia adecuada según el tipo de transacción.
 */
@Component
public class TransactionStrategyFactory {
  private final Map<String, Function<TransactionDTO, Mono<Object>>> strategies = new HashMap<>();

  /**
   * Constructor de TransactionStrategyFactory.
   *
   * @param transactionStrategy objeto que contiene las estrategias disponibles para las transacciones
   */
  public TransactionStrategyFactory(TransactionStrategy transactionStrategy) {
    strategies.put("DEPOSITO", transactionStrategy::depositStrategy);
    strategies.put("RETIRO", transactionStrategy::withdrawalStrategy);
    strategies.put("CARGO", transactionStrategy::creditCardStrategy);
    strategies.put("PAGO", transactionStrategy::creditStrategy);
  }

  /**
   * Obtiene la estrategia correspondiente para el tipo de transacción proporcionado.
   *
   * @param transactionType el tipo de transacción
   * @return la estrategia correspondiente como una función Function
   */
  public Function<TransactionDTO, Mono<Object>> getStrategy(String transactionType) {
    return strategies.get(transactionType);
  }
}