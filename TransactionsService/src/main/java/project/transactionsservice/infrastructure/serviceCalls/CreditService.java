package project.transactionsservice.infrastructure.serviceCalls;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import project.transactionsservice.infrastructure.serviceCalls.webClient.CreditWebInterface;

@Component
public class CreditService implements CreditWebInterface {
  private final WebClient.Builder webClient;
  private String serviceName; // Nombre del servicio de cuentas bancarias (Registrado en Eureka)

  public CreditService(WebClient.Builder webClient) {
    this.webClient = webClient;
  }
}
