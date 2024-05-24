package project.transactionsservice.infrastructure.factory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.servicecalls.responses.AccountResponse;
import project.transactionsservice.infrastructure.strategies.AccountStrategies;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.BiFunction;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class AccountsFactoryTest {

  @Mock
  private AccountStrategies accountStrategies;

  @InjectMocks
  private AccountsFactory accountsFactory;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    when(accountStrategies.fxdStrategy(any(TransactionDTO.class), any(AccountResponse.class)))
        .thenReturn(Mono.just(new TransactionDTO()));
    when(accountStrategies.savingsStrategy(any(TransactionDTO.class), any(AccountResponse.class)))
        .thenReturn(Mono.just(new TransactionDTO()));
    when(accountStrategies.currAccStrategy(any(TransactionDTO.class), any(AccountResponse.class)))
        .thenReturn(Mono.just(new TransactionDTO()));
  }

  @Test
  void testGetAccountStrategy() {
    BiFunction<TransactionDTO, AccountResponse, Mono<TransactionDTO>> strategy = accountsFactory.getStrategy("PLAZO_FIJO");
    StepVerifier.create(strategy.apply(new TransactionDTO(), new AccountResponse()))
        .expectNextCount(1)
        .verifyComplete();
  }

  @Test
  void testGetStrategyForUnknownType() {
    BiFunction<TransactionDTO, AccountResponse, Mono<TransactionDTO>> strategy = accountsFactory.getStrategy("UNKNOWN");
    Assertions.assertNull(strategy);
  }
}