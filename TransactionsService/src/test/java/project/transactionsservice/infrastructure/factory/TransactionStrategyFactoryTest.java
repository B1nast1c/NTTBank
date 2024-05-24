package project.transactionsservice.infrastructure.factory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.strategies.TransactionStrategy;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TransactionStrategyFactoryTest {
  @Mock
  private TransactionStrategy transactionStrategy;

  @InjectMocks
  private TransactionStrategyFactory transactionStrategyFactory;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetDepositStrategy() {
    TransactionDTO transactionDTO = new TransactionDTO();
    transactionDTO.setTransactionType("DEPOSITO");

    when(transactionStrategy.depositStrategy(any(TransactionDTO.class))).thenReturn(Mono.just(new Object()));

    Function<TransactionDTO, Mono<Object>> strategy = transactionStrategyFactory.getStrategy("DEPOSITO");
    StepVerifier.create(strategy.apply(transactionDTO))
        .expectNextCount(1)
        .verifyComplete();
  }

  @Test
  void testGetWithdrawalStrategy() {
    TransactionDTO transactionDTO = new TransactionDTO();
    transactionDTO.setTransactionType("RETIRO");

    when(transactionStrategy.withdrawalStrategy(any(TransactionDTO.class))).thenReturn(Mono.just(new Object()));

    Function<TransactionDTO, Mono<Object>> strategy = transactionStrategyFactory.getStrategy("RETIRO");
    StepVerifier.create(strategy.apply(transactionDTO))
        .expectNextCount(1)
        .verifyComplete();
  }

  @Test
  void testGetStrategyForUnknownType() {
    Function<TransactionDTO, Mono<Object>> strategy = transactionStrategyFactory.getStrategy("UNKNOWN");
    Assertions.assertNull(strategy);
  }
}

