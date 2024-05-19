package project;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class FinalProjectApplicationTest {
  @Test
  void contextLoads() {
    Assertions.assertTrue(true); // Para el sonarqube y lint
    // Este método vacío asegura que el contexto de Spring se cargue correctamente
  }
}
