package project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@EnableDiscoveryClient
@SpringBootApplication
@EnableReactiveMongoRepositories
public class FinalProjectApplication {
  public static void main(String[] args) {
    System.setProperty("spring.cloud.bootstrap.enabled", "true");
    SpringApplication.run(FinalProjectApplication.class, args);
  }
}
