package project.infrastructure.clientcalls;

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
import project.infrastructure.clientcalls.responses.Client;
import project.infrastructure.clientcalls.responses.ClientResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

class WebClientSrvTest {
  private static WireMockServer server;
  private WebClientSrv clientSrv;

  @Mock
  private WebClient.Builder webClientBuilder;

  @BeforeAll
  static void startWireMock() {
    server = new WireMockServer(8083);
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

    WebClient webClient = WebClient.builder().baseUrl("http://localhost:8083").build();
    when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
    when(webClientBuilder.build()).thenReturn(webClient);

    clientSrv = new WebClientSrv(webClientBuilder);
  }

  @SneakyThrows
  @Test
  void shouldReturnClient() {
    String clientId = "testDocument";
    Client expectedClient = new Client("testID", "PERSONAL", "testName", "testAddress", "testEmail", "testPhone", "testDocument", true);
    ClientResponse expectedResponse = new ClientResponse(true, expectedClient);
    ObjectMapper objectMapper = new ObjectMapper();
    String expectedJson = objectMapper.writeValueAsString(expectedResponse);

    stubFor(get(urlPathEqualTo("/clients/" + clientId))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody(expectedJson)));
    
    Mono<ClientResponse> result = clientSrv.getClientByiD(clientId);

    StepVerifier.create(result)
        .assertNext(res -> {
          assertTrue(res.isSuccess());
          assertEquals(res.getData().getDocumentNumber(), expectedClient.getDocumentNumber());
          assertEquals(res.getData().getClientName(), expectedClient.getClientName());
          assertEquals(res.getData().getClientPhone(), expectedClient.getClientPhone());
        })
        .verifyComplete();
  }

  @Test
  void shouldHandleServerError() {
    String clientId = "anyDocument";
    stubFor(get(urlPathEqualTo("/clients/" + clientId))
        .willReturn(aResponse()
            .withStatus(500)));

    Mono<ClientResponse> result = clientSrv.getClientByiD(clientId);

    StepVerifier.create(result)
        .expectError(RuntimeException.class)
        .verify();
  }
}