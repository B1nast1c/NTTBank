package project.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuración para un WebClient reactivo en un entorno de balanceo de carga.
 */
@Configuration
public class WebClientConfig {
  @Value("${client.service.baseUrl}")
  private String clientServiceBaseUrl;

  @Bean
  @LoadBalanced
  public WebClient.Builder loadBalancedWebClientBuilder() {
    return WebClient.builder()
        .baseUrl(clientServiceBaseUrl); // URL base del servicio de cliente
  }
}
