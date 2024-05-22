package project.infrastructure.clientcalls;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;

class WebClientSrvTest {
  private final WebClient.Builder webClient = Mockito.mock(WebClient.Builder.class);
  private final WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
  private final WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

  // Estas pruebas se efectuan teniendo al servicio de cliente arriba
  // Implementar despues
  @Test
  void shouldReturnSuccess() {

  }

  @Test
  void shouldReturnError() {

  }
}
