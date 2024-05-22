package project.infrastructure.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuración para un WebClient reactivo en un entorno de balanceo de carga.
 */
@Configuration
public class WebClientConfig {
  @Bean
  @LoadBalanced
  public WebClient.Builder loadBalancedWebClientBuilder() {
    return WebClient.builder()
        .baseUrl("http://clientService"); // URL base del servicio de cliente
  }
}
