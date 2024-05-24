package project.transactionsservice.application.controller;

import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import project.transactionsservice.application.service.TransactionsService;
import project.transactionsservice.domain.model.Transaction;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.exceptions.CustomResponse;
import project.transactionsservice.infrastructure.mapper.GenericMapper;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class TransactionsControllerTest {
  private final MockWebServer mockWebServer = new MockWebServer();
  private final TransactionDTO transactionDTO = new TransactionDTO(
      "testID",
      "productID",
      "RETIRO",
      "2024-12-12",
      "cientID",
      "No details",
      2500.0
  );
  private final Transaction transaction = GenericMapper.mapToAny(transactionDTO, Transaction.class);
  private final CustomResponse customResponse = new CustomResponse<>(true, transaction);

  @Mock
  private WebTestClient webTestClient;

  @Mock
  private TransactionsService transactionsService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    webTestClient = WebTestClient.bindToController(new TransactionsController(transactionsService)).build();
  }

  @Test
  void shouldGetTransactions() {
    List<TransactionDTO> transactions = List.of(transactionDTO);
    customResponse.setData(transactions);
    given(transactionsService.getAllTransactions())
        .willReturn(Mono.just(customResponse));

    WebTestClient.ResponseSpec response = webTestClient
        .get().uri("/transactions/all")
        .exchange();

    response
        .expectStatus().isOk()
        .expectBody()
        .consumeWith(System.out::println)
        .jsonPath("$.success").isEqualTo(true);
  }

  @Test
  void shouldCreateTransaction() {
    customResponse.setData(transactionDTO);
    when(transactionsService.createTransaction(transactionDTO)).thenReturn(Mono.just(customResponse));

    WebTestClient.ResponseSpec response = webTestClient.post()
        .uri("/transactions/create")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(transactionDTO), TransactionDTO.class)
        .exchange();

    response
        .expectStatus().isOk()
        .expectBody(CustomResponse.class)
        .consumeWith(System.out::println);
  }

  @Test
  void shouldGetTransactionById() {
    CustomResponse<Object> response = new CustomResponse<>(true, transactionDTO);

    when(transactionsService.getTransaction(anyString())).thenReturn(Mono.just(response));

    webTestClient.get()
        .uri("/transactions/transaction/transactionId")
        .exchange()
        .expectStatus().isOk()
        .expectBody(CustomResponse.class)
        .consumeWith(System.out::println);
  }

  @Test
  void shouldGetTransactionsByProduct() {
    List<TransactionDTO> transactionList = List.of(transactionDTO);
    CustomResponse<Object> response = new CustomResponse<>(true, transactionList);

    when(transactionsService.getAllTransactionsByProduct(anyString())).thenReturn(Mono.just(response));

    webTestClient.get()
        .uri("/transactions/bankProduct/productNumber")
        .exchange()
        .expectStatus().isOk()
        .expectBody(CustomResponse.class)
        .consumeWith(System.out::println);
  }
}
