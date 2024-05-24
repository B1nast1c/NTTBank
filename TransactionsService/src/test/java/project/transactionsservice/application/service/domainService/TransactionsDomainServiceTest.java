package project.transactionsservice.application.service.domainService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import project.transactionsservice.domain.ports.TransactionsPort;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.exceptions.CustomError;
import project.transactionsservice.infrastructure.exceptions.CustomResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class TransactionsDomainServiceTest {
  private final TransactionDTO transactionDTO = new TransactionDTO();

  @Mock
  private TransactionsPort transactionsPort;

  @InjectMocks
  private TransactionsDomainService transactionsDomainService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    when(transactionsPort.saveTransaction(any(TransactionDTO.class))).thenReturn(Mono.just(transactionDTO));
    when(transactionsPort.getTransaction(anyString())).thenReturn(Mono.just(transactionDTO));
    when(transactionsPort.getAllTransactions()).thenReturn(Flux.just(transactionDTO));
    when(transactionsPort.getTransactionsByProductNumber(anyString())).thenReturn(Flux.just(transactionDTO));
  }

  @Test
  void shouldCreateTransactionAndReturnSuccessResponse() {
    Mono<CustomResponse<Object>> result = transactionsDomainService.createTransaction(transactionDTO);

    StepVerifier.create(result)
        .expectNextMatches(response -> response.isSuccess() && response.getData().equals(transactionDTO))
        .verifyComplete();
  }

  @Test
  void shouldNotCreateTransactionAndReturnErrorResponse() {
    String errorMessage = "Error saving transaction";
    when(transactionsPort.saveTransaction(any(TransactionDTO.class)))
        .thenReturn(Mono.error(new RuntimeException(errorMessage)));

    Mono<CustomResponse<Object>> result = transactionsDomainService.createTransaction(new TransactionDTO());

    StepVerifier.create(result)
        .expectNextMatches(response -> !response.isSuccess() && ((CustomError) response.getData()).getErrorMessage().equals(errorMessage))
        .verifyComplete();
  }

  @Test
  void shouldGetTransactionAndReturnSuccessResponse() {
    Mono<CustomResponse<Object>> result = transactionsDomainService.getTransaction("transactionId");

    StepVerifier.create(result)
        .expectNextMatches(response -> response.isSuccess() && response.getData().equals(transactionDTO))
        .verifyComplete();
  }

  @Test
  void shouldNotGetTransactionAndReturnErrorResponse() {
    String errorMessage = "Error getting transaction";
    when(transactionsPort.getTransaction(anyString()))
        .thenReturn(Mono.error(new RuntimeException(errorMessage)));

    Mono<CustomResponse<Object>> result = transactionsDomainService.getTransaction("transactionId");

    StepVerifier.create(result)
        .expectNextMatches(response -> !response.isSuccess() && ((CustomError) response.getData()).getErrorMessage().equals(errorMessage))
        .verifyComplete();
  }

  @Test
  void shouldGetAllTransactionsAndReturnSuccessResponse() {
    Mono<CustomResponse<List<TransactionDTO>>> result = transactionsDomainService.getAllTransactions();

    StepVerifier.create(result)
        .expectNextMatches(response ->
            response.isSuccess() && response.getData().size() == 1
                && response.getData().get(0).equals(transactionDTO))
        .verifyComplete();
  }

  @Test
  void shouldGetTransactionsByProductNumberAndReturnSuccessResponse() {
    TransactionDTO transactionDTO = new TransactionDTO();

    Mono<CustomResponse<Object>> result = transactionsDomainService.getAllTransactionsByProduct("productNumber");

    StepVerifier.create(result)
        .assertNext(res -> {
          assert res.isSuccess();
        })
        .verifyComplete();
  }

  @Test
  void shouldNotGetAllTransactionsAndReturnErrorResponse() {
    String errorMessage = "Error getting transactions by product number";
    when(transactionsPort.getTransactionsByProductNumber(anyString()))
        .thenReturn(Flux.error(new RuntimeException(errorMessage)));

    Mono<CustomResponse<Object>> result = transactionsDomainService.getAllTransactionsByProduct("productNumber");

    StepVerifier.create(result)
        .expectNextMatches(response -> !response.isSuccess() && ((CustomError) response.getData()).getErrorMessage().equals(errorMessage))
        .verifyComplete();
  }
}
