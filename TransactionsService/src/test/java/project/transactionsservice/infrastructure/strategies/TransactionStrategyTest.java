package project.transactionsservice.infrastructure.strategies;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.web.reactive.function.client.WebClient;
import project.transactionsservice.infrastructure.adapters.mongoRepos.TransactionsRepo;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.exceptions.throwable.InvalidTransaction;
import project.transactionsservice.infrastructure.factory.AccountsFactory;
import project.transactionsservice.infrastructure.helpers.HelperFunctions;
import project.transactionsservice.infrastructure.mapper.GenericMapper;
import project.transactionsservice.infrastructure.servicecalls.AccountService;
import project.transactionsservice.infrastructure.servicecalls.request.AccountRequest;
import project.transactionsservice.infrastructure.servicecalls.responses.AccountResponse;
import project.transactionsservice.infrastructure.servicecalls.responses.GenericResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

class TransactionStrategyTest {
  private static WireMockServer server;
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final TransactionDTO transactionDTO = new TransactionDTO(
      "testTransactionNumber",
      "testProductNumber",
      "DEPOSITO",
      "2023-07-01",
      "clientNumber",
      "testDetail",
      200.0);
  List<String> titulars = new ArrayList<>();
  List<Object> legals = new ArrayList<>();
  private final AccountResponse accountResponse = new AccountResponse(
      "accountNumber",
      "AHORRO",
      "clientNumber",
      500,
      0,
      0,
      titulars,
      legals,
      Date.valueOf("2023-07-01"),
      10
  );

  @Mock
  private WebClient.Builder webClientBuilder;

  @Mock
  private AccountsFactory accountsFactory;

  @Mock
  private HelperFunctions helpers;

  @Mock
  private AccountService accountService;

  @Mock
  private TransactionsRepo transactionsRepo;

  private final AccountStrategies strategies = new AccountStrategies(transactionsRepo);
  private final BiFunction<TransactionDTO, AccountResponse, Mono<TransactionDTO>> strategy = strategies::savingsStrategy;

  @Spy
  @InjectMocks
  private TransactionStrategy transactionStrategy;

  @BeforeAll
  static void startWireMock() {
    server = new WireMockServer(0);
    server.start();
    WireMock.configureFor(server.port());
  }

  @AfterAll
  static void stopWireMock() {
    if (server != null) {
      server.stop();
    }
  }

  @SneakyThrows
  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    WebClient webClient = WebClient.builder().baseUrl("http://localhost:" + server.port()).build();
    when(webClientBuilder.baseUrl(any())).thenReturn(webClientBuilder);
    when(webClientBuilder.build()).thenReturn(webClient);
  }

  @Test
  @SneakyThrows
  void shouldAllowDeposit() {
    AccountRequest requestBody = new AccountRequest(300.0, 2);
    GenericResponse expectedResponse = new GenericResponse(true, transactionDTO);
    String requestJson = objectMapper.writeValueAsString(requestBody);
    String expectedJson = objectMapper.writeValueAsString(accountResponse);

    when(helpers.getAccountDetails(any())).thenReturn(Mono.just(accountResponse));
    when(accountsFactory.getStrategy(accountResponse.getAccountType())).thenReturn(strategies::savingsStrategy);
    strategy.apply(transactionDTO, accountResponse);
    when(accountService.updateAccount(anyString(), any(AccountRequest.class))).thenReturn(Mono.just(expectedResponse));

    stubFor(patch(urlEqualTo("/account/update/" + transactionDTO.getProductNumber()))
        .withRequestBody(equalToJson(requestJson))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody(expectedJson)));

    Mono<Object> result = transactionStrategy.depositStrategy(transactionDTO);

    StepVerifier.create(result)
        .expectNextMatches(response -> {
          TransactionDTO mappedResponse = GenericMapper.mapToAny(response, TransactionDTO.class);
          assert mappedResponse.getClientNumber().equals(transactionDTO.getClientNumber());
          assert mappedResponse.getProductNumber().equals(transactionDTO.getProductNumber());
          assert mappedResponse.getTransactionType().equals(transactionDTO.getTransactionType());
          return true;
        })
        .verifyComplete();
    verify(accountsFactory).getStrategy(accountResponse.getAccountType());
  }

  @Test
  @SneakyThrows
  void shouldUpdateWithdrawal() {
    transactionDTO.setTransactionType("RETIRO");
    AccountRequest requestBody = new AccountRequest(300.0, 2);
    GenericResponse expectedResponse = new GenericResponse(true, transactionDTO);
    String requestJson = objectMapper.writeValueAsString(requestBody);
    String expectedJson = objectMapper.writeValueAsString(accountResponse);

    when(helpers.getAccountDetails(any())).thenReturn(Mono.just(accountResponse));
    when(accountsFactory.getStrategy(accountResponse.getAccountType())).thenReturn(strategies::savingsStrategy);
    strategy.apply(transactionDTO, accountResponse);
    when(accountService.updateAccount(anyString(), any(AccountRequest.class))).thenReturn(Mono.just(expectedResponse));

    stubFor(patch(urlEqualTo("/account/update/" + transactionDTO.getProductNumber()))
        .withRequestBody(equalToJson(requestJson))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody(expectedJson)));

    Mono<Object> result = transactionStrategy.withdrawalStrategy(transactionDTO);

    StepVerifier.create(result)
        .expectNextMatches(response -> {
          TransactionDTO mappedResponse = GenericMapper.mapToAny(response, TransactionDTO.class);
          assert mappedResponse.getClientNumber().equals(transactionDTO.getClientNumber());
          assert mappedResponse.getProductNumber().equals(transactionDTO.getProductNumber());
          assert mappedResponse.getTransactionType().equals(transactionDTO.getTransactionType());
          return true;
        })
        .verifyComplete();
    verify(accountsFactory).getStrategy(accountResponse.getAccountType());
  }

  @Test
  @SneakyThrows
  void shouldNotUpdateWithdrawal() {
    transactionDTO.setTransactionType("RETIRO");
    AccountRequest requestBody = new AccountRequest(300.0, 2);
    GenericResponse expectedResponse = new GenericResponse(false, transactionDTO);
    String requestJson = objectMapper.writeValueAsString(requestBody);
    String expectedJson = objectMapper.writeValueAsString(accountResponse);

    when(helpers.getAccountDetails(any())).thenReturn(Mono.just(accountResponse));
    when(accountsFactory.getStrategy(accountResponse.getAccountType())).thenReturn(strategies::savingsStrategy);
    strategy.apply(transactionDTO, accountResponse);
    when(accountService.updateAccount(anyString(), any(AccountRequest.class))).thenReturn(Mono.just(expectedResponse));

    stubFor(patch(urlEqualTo("/account/update/" + transactionDTO.getProductNumber()))
        .withRequestBody(equalToJson(requestJson))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody(expectedJson)));

    Mono<Object> result = transactionStrategy.depositStrategy(transactionDTO);

    StepVerifier.create(result)
        .expectError(InvalidTransaction.class)
        .verify();
    verify(accountsFactory).getStrategy(accountResponse.getAccountType());
  }
}
