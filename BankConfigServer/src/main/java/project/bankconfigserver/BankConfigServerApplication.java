package project.bankconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class BankConfigServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(BankConfigServerApplication.class, args);
  }

}
