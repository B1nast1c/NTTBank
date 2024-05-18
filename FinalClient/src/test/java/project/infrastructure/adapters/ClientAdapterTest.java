package infrastructure.adapters;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import project.FinalProjectApplication;
import project.domain.model.Client;
import project.infrastructure.adapters.ClientAdapter;
import project.infrastructure.adapters.mongoRepos.ClientRepository;
import project.infrastructure.dto.ClientDTO;
import project.infrastructure.mapper.GenericMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = FinalProjectApplication.class)
@RunWith(SpringRunner.class)
public class ClientAdapterTest {
  @Autowired
  private ClientAdapter clientRepo;

  @Mock
  private ClientRepository clientRepoMock;

  @Test
  void shouldFindByDocument() {
    String documentNumber = "123456789";

    ClientDTO client = new ClientDTO();
    client.setCustomId("1");
    client.setClientType("EMPRESARIAL");
    client.setClientName("John Doe");
    client.setClientAddress("123 Main St");
    client.setClientEmail("john@example.com");
    client.setClientPhone("123456789");
    client.setDocumentNumber(documentNumber);

    Client savedClient = clientRepo
        .save(client)
        .map(GenericMapper::mapToEntity)
        .block();

    Mono<Client> foundClient = clientRepo
        .findByID(savedClient.getDocumentNumber())
        .map(GenericMapper::mapToEntity);

    StepVerifier.create(foundClient)
        .assertNext(item -> {
          Assertions.assertEquals("Jonh Doe", item.getClientName());
          Assertions.assertEquals(documentNumber, item.getDocumentNumber());
          Assertions.assertNotNull(item.getCustomId());
        })
        .expectComplete()
        .verify();

  }
}
