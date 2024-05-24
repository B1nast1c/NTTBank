package project.transactionsservice.infrastructure.servicecalls;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import project.transactionsservice.infrastructure.servicecalls.webClient.CreditWebInterface;

@Component
public class CreditService implements CreditWebInterface {
  private final WebClient.Builder webClient;

  public CreditService(WebClient.Builder webClient) {
    this.webClient = webClient;
  }
}
