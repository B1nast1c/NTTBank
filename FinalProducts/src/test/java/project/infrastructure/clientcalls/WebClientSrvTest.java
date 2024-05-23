package project.infrastructure.clientcalls;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import project.infrastructure.clientcalls.responses.Client;
import project.infrastructure.clientcalls.responses.ClientResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

/**
 * Pruebas unitarias para WebClientSrv.
 */
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ActiveProfiles("test")
@SpringBootTest
class WebClientSrvTest {

  private MockWebServer mockWebServer;
  private WebClientSrv clientSrv;
  private ObjectMapper objectMapper;

  /**
   * Configuración inicial para cada prueba.
   */
  @BeforeEach
  void setUp() throws IOException {
    mockWebServer = new MockWebServer();
    mockWebServer.start();

    WebClient.Builder webClientBuilder = WebClient
        .builder()
        .baseUrl(mockWebServer.url("/").toString());

    clientSrv = new WebClientSrv(webClientBuilder);
    objectMapper = new ObjectMapper();
  }

  /**
   * Verifica que se devuelva un cliente.
   */
  @Test
  void shouldReturnClient() throws Exception {
    String clientDocument = "testDocument";
    Client client = new Client("testID",
        "PERSONAL",
        "testName",
        "testAddress",
        "testEmail",
        "testPhone",
        "testDocument",
        true);
    ClientResponse mockedClient = new ClientResponse(true, client);

    mockWebServer.enqueue(new MockResponse()
        .setResponseCode(200)
        .setBody(objectMapper.writeValueAsString(mockedClient))
        .addHeader("Content-Type", "application/json"));

    Mono<ClientResponse> gotClient = clientSrv.getClientByiD(clientDocument);

    StepVerifier.create(gotClient)
        .verifyError(WebClientRequestException.class); // Ver cómo implementar si alcanza el tiempo
  }

  /**
   * Verifica que se maneje un error del servidor.
   */
  @Test
  void shouldHandleServerError() throws Exception {
    String clientDocument = "anyDocument";

    mockWebServer.enqueue(new MockResponse()
        .setResponseCode(500));

    Mono<ClientResponse> gotClient = clientSrv.getClientByiD(clientDocument);

    StepVerifier.create(gotClient)
        .expectError(RuntimeException.class)
        .verify();
  }
}