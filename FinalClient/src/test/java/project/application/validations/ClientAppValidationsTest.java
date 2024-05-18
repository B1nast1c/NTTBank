package project.application.validations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import project.infrastructure.adapters.mongoRepos.ClientRepository;
import project.infrastructure.dto.ClientDTO;
import project.infrastructure.exceptions.throwable.InvalidDocument;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ClientAppValidationsTest {

  ClientDTO testDTO = new ClientDTO();
  @Mock
  private ClientRepository clientRepository;
  @InjectMocks
  private ClientAppValidations clientAppValidations;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void shouldBeValidated() {
    testDTO.setDocumentNumber("111222333");
    when(clientRepository.existsByDocumentNumber(anyString())).thenReturn(Mono.just(false));
    Mono<ClientDTO> result = clientAppValidations.validateDocumentNumber(testDTO);

    StepVerifier.create(result)
        .expectNext(testDTO)
        .verifyComplete();
  }

  @Test
  public void shouldBeInvalidated() {
    testDTO.setDocumentNumber("111222333");
    when(clientRepository.existsByDocumentNumber(anyString())).thenReturn(Mono.just(true));

    Mono<ClientDTO> result = clientAppValidations.validateDocumentNumber(testDTO);

    StepVerifier.create(result)
        .expectErrorMatches(throwable -> throwable instanceof InvalidDocument &&
            throwable.getMessage().equals("The provided document number is not valid"))
        .verify();
  }
}
