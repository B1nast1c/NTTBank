package project.transactionsservice.infrastructure.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import project.transactionsservice.domain.model.Transaction;
import project.transactionsservice.domain.model.TransactionType;
import project.transactionsservice.infrastructure.adapters.mongoRepos.TransactionsRepo;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.exceptions.throwable.InvalidTransactionType;
import project.transactionsservice.infrastructure.exceptions.throwable.NotFoundTransaction;
import project.transactionsservice.infrastructure.factory.TransactionStrategyFactory;
import project.transactionsservice.infrastructure.mapper.GenericMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.sql.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TransactionsAdapterTest {
  @Mock
  private TransactionStrategyFactory transactionsFactory;

  @Mock
  private TransactionsRepo transactionsRepo;

  @InjectMocks
  private TransactionsAdapter transactionsAdapter;

  private TransactionDTO transactionDTO;
  private Transaction transaction;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    transaction = new Transaction(
        "testTransactionNumber",
        "testProductNumber",
        TransactionType.DEPOSITO,
        100.0,
        Date.valueOf("2023-07-01"),
        "testClient",
        "detail");
    transactionDTO = GenericMapper.mapToDto(transaction);

    when(transactionsFactory.getStrategy(anyString())).thenReturn(dto -> Mono.just(transactionDTO));
    when(transactionsRepo.save(Mockito.any(Transaction.class))).thenReturn(Mono.just(transaction));
    when(transactionsRepo.findById(anyString())).thenReturn(Mono.just(transaction));
    when(transactionsRepo.findAll()).thenReturn(Flux.just(transaction));
    when(transactionsRepo.findAllByProductNumber("testProductNumber")).thenReturn(Flux.just(transaction));
  }

  @Test
  void shouldSaveTransaction() {
    Mono<Object> result = transactionsAdapter.saveTransaction(transactionDTO);

    StepVerifier.create(result)
        .expectNextMatches(savedTransaction -> {
          assertThat(savedTransaction).isInstanceOf(TransactionDTO.class);
          TransactionDTO savedTransactionDTO = GenericMapper.mapToAny(savedTransaction, TransactionDTO.class);
          assertThat(savedTransactionDTO.getTransactionId())
              .isEqualTo("testTransactionNumber");
          return true;
        })
        .verifyComplete();

    verify(transactionsFactory, times(1)).getStrategy("DEPOSITO");
    verify(transactionsRepo, times(1)).save(any(Transaction.class));
  }

  @Test
  void shouldNotSaveTransactionWrongStrategy() {
    transactionDTO.setTransactionType("INVALID");
    when(transactionsFactory.getStrategy(anyString())).thenReturn(null);

    Mono<Object> result = transactionsAdapter.saveTransaction(transactionDTO);

    StepVerifier.create(result)
        .expectError(InvalidTransactionType.class)
        .verify();

    verify(transactionsRepo, never()).save(any(Transaction.class));
  }

  @Test
  void shouldGetTransaction() {
    Mono<TransactionDTO> result = transactionsAdapter.getTransaction("testTransactionNumber");

    StepVerifier.create(result)
        .expectNextMatches(transactionDTO -> {
          assertThat(transactionDTO.getTransactionId()).isEqualTo("testTransactionNumber");
          return true;
        })
        .verifyComplete();

    verify(transactionsRepo, times(1)).findById("testTransactionNumber");
  }

  @Test
  void shouldNotGetTransaction() {
    when(transactionsRepo.findById(anyString())).thenReturn(Mono.empty());
    Mono<TransactionDTO> result = transactionsAdapter.getTransaction("testTransactionNumber");

    StepVerifier.create(result)
        .expectError(NotFoundTransaction.class)
        .verify();

    verify(transactionsRepo, times(1)).findById("testTransactionNumber");
  }

  @Test
  void testGetAllTransactions() {
    Flux<TransactionDTO> result = transactionsAdapter.getAllTransactions();

    StepVerifier.create(result)
        .expectNextMatches(transactionDTO -> {
          assertThat(transactionDTO.getTransactionId()).isEqualTo("testTransactionNumber");
          return true;
        })
        .verifyComplete();

    verify(transactionsRepo, times(1)).findAll();
  }

  @Test
  void testGetTransactionsByProductNumber() {
    Flux<TransactionDTO> result = transactionsAdapter.getTransactionsByProductNumber("testProductNumber");

    StepVerifier.create(result)
        .expectNextMatches(transactionDTO -> {
          assertThat(transactionDTO.getProductNumber()).isEqualTo("testProductNumber");
          return true;
        })
        .verifyComplete();

    verify(transactionsRepo, times(1)).findAllByProductNumber("testProductNumber");
  }
}