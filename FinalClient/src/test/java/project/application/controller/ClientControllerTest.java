package project.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import project.infrastructure.dto.ClientDTO;
import project.infrastructure.exceptions.CustomError;
import project.infrastructure.responses.CustomResponse;

import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientControllerTest {
  private final MockWebServer mockWebServer = new MockWebServer();
  @Autowired
  private WebTestClient webTestClient;
  @Autowired
  private ApplicationContext context;

  @Test
  void shouldReturnAll() throws IOException {
    mockWebServer.enqueue(new MockResponse().setResponseCode(200));

    webTestClient.get().uri("clients/all")
        .exchange()
        .expectStatus().isOk();
  }

  @Test
  void shouldReturnErrorClientById() throws IOException {
    CustomError customError = new CustomError("Client with document number 123 not found", CustomError.ErrorType.GENERIC_ERROR);
    String responseBody = new ObjectMapper().writeValueAsString(
        new CustomResponse<>(false, customError)
    );

    mockWebServer.enqueue(new MockResponse().setBody(responseBody).setResponseCode(200));

    webTestClient.get().uri("/clients/123")
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class).isEqualTo(responseBody);
  }

  @Test
  void shouldClientByDocumentNumber() throws IOException {
    mockWebServer.enqueue(new MockResponse().setResponseCode(200));

    webTestClient.get().uri("/clients/72163252")
        .exchange()
        .expectStatus().isOk();
  }

  @Test
  void shouldAddClient() throws IOException {
    mockWebServer.enqueue(new MockResponse().setResponseCode(200));

    webTestClient.post().uri("clients/create")
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(new ClientDTO("1", "PERSONAL", "name", "address", "mail", "phone", "987654321", true, "date")))
        .exchange()
        .expectStatus().isOk();
  }

  @Test
  void shouldNotAddClient() throws IOException {
    CustomError customError = new CustomError("Client type must be PERSONAL or EMPRESARIAL", CustomError.ErrorType.GENERIC_ERROR);
    String responseBody = new ObjectMapper().writeValueAsString(
        new CustomResponse<>(false, customError)
    );
    mockWebServer.enqueue(new MockResponse().setResponseCode(200));

    webTestClient.post().uri("clients/create")
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(new ClientDTO("1", "INVALIDO", "name", "address", "mail", "phone", "987654321", true, "date")))
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class).isEqualTo(responseBody);
  }

  @Test
  void shouldDeleteClient() throws IOException {
    mockWebServer.enqueue(new MockResponse().setResponseCode(200));
    webTestClient.delete().uri("/clients/delete/6647d2845c2e024d522f8e83")
        .exchange()
        .expectStatus().isOk();
  }

  @Test
  void shouldNotDeleteClient() throws IOException {
    CustomError customError = new CustomError("Client with ID badID not found", CustomError.ErrorType.GENERIC_ERROR);
    String responseBody = new ObjectMapper().writeValueAsString(
        new CustomResponse<>(false, customError)
    );

    mockWebServer.enqueue(new MockResponse().setResponseCode(200));
    webTestClient.delete().uri("/clients/delete/badID")
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class).isEqualTo(responseBody);
  }
}
