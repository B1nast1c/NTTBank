package project.transactionsservice.infrastructure.servicecalls;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import project.transactionsservice.domain.model.Transaction;
import project.transactionsservice.domain.model.TransactionType;
import project.transactionsservice.infrastructure.mapper.GenericMapper;
import project.transactionsservice.infrastructure.servicecalls.request.AccountRequest;
import project.transactionsservice.infrastructure.servicecalls.responses.GenericResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.sql.Date;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

class AccountServiceTest {
  private static WireMockServer server;
  private AccountService accountService;
  private Transaction expectedTransaction;

  @Mock
  private WebClient.Builder webClientBuilder;

  @BeforeAll
  static void startWireMock() {
    server = new WireMockServer(8085);
    server.start();
    configureFor(server.port());
  }

  @AfterAll
  static void stopWireMock() {
    if (server != null) {
      server.stop();
    }
  }

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    WebClient webClient = WebClient.builder().baseUrl("http://localhost:8085").build();
    when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
    when(webClientBuilder.build()).thenReturn(webClient);

    accountService = new AccountService(webClientBuilder);
    expectedTransaction = new Transaction(
        "testTransactionNumber",
        "testProductNumber",
        TransactionType.DEPOSITO,
        100.0,
        Date.valueOf("2023-07-01"),
        "testClient",
        "detail");
  }

  @SneakyThrows
  @Test
  void shouldReturnAccount() {
    String transactionId = "testAccount";
    GenericResponse expectedResponse = new GenericResponse(true, expectedTransaction);
    ObjectMapper objectMapper = new ObjectMapper();
    String expectedJson = objectMapper.writeValueAsString(expectedResponse);

    stubFor(get(urlPathEqualTo("/accounts/account/" + transactionId))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody(expectedJson)));

    Mono<GenericResponse> result = accountService.getAccount(transactionId);

    StepVerifier.create(result)
        .assertNext(res -> {
          assertTrue(res.isSuccess());
          Transaction actualTransaction = GenericMapper.mapToAny(res.getData(), Transaction.class);
          assertEquals(actualTransaction.getClientNumber(), expectedTransaction.getClientNumber());
          assertEquals(actualTransaction.getTransactionId(), expectedTransaction.getTransactionId());
        })
        .verifyComplete();
  }

  @SneakyThrows
  @Test
  void shouldUpdateTransaction() {
    String transactionId = "testAccount";
    AccountRequest accountRequest = new AccountRequest(200.0, 1);
    ObjectMapper mapper = new ObjectMapper();
    String requestJson = mapper.writeValueAsString(accountRequest);
    GenericResponse expectedResponse = new GenericResponse(true, expectedTransaction);
    ObjectMapper objectMapper = new ObjectMapper();
    String expectedJson = objectMapper.writeValueAsString(expectedResponse);

    stubFor(patch
        (urlPathEqualTo("/accounts/update/" + transactionId))
        .withRequestBody(equalToJson(requestJson))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody(expectedJson)));

    Mono<GenericResponse> result = accountService.updateAccount(transactionId, accountRequest);

    StepVerifier.create(result)
        .assertNext(res -> {
          assertTrue(res.isSuccess());
          Transaction actualTransaction = GenericMapper.mapToAny(res.getData(), Transaction.class);
          assertEquals(actualTransaction.getClientNumber(), expectedTransaction.getClientNumber());
          assertEquals(actualTransaction.getTransactionId(), expectedTransaction.getTransactionId());
        })
        .verifyComplete();
  }
}
