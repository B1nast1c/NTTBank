package project.application.service.DomainService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import project.domain.model.Client;
import project.domain.ports.ClientPort;
import project.infrastructure.adapters.mongoRepos.ClientRepository;
import project.infrastructure.dto.ClientDTO;
import project.infrastructure.exceptions.CustomError;
import project.infrastructure.responses.CustomResponse;
import project.infrastructure.exceptions.throwable.NotFound;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


/**
 * Testing para la IMPLEMENTACIÓN DEl SERVICIO.
 * NOTAS PARA EL LECTOR XD -> Cada falla se prueba con un error genérico,
 * por que al revisar el archivo "DomainClientService" ese es el comportamiento
 * por defecto que se obtiene cuando algún error salta a la luz
 * (CustomError.ErrorType.GENERIC_ERROR) adicionalmente, TODAS las excepciones son de tipo RUNTIME
 * pues todos los tipos de errores contemplados dentro paquete
 * project.application.infrastructure.exceptions.throwable heredan de ese error.
 */
class DomainClientServiceTest {
  ClientDTO testDTO = new ClientDTO();
  ClientDTO testDTO2 = new ClientDTO();
  Client test = new Client();
  String clientId = "111222333";

  @Mock
  private ClientPort clientPort;

  @Mock
  private ClientRepository clientRepository;

  @InjectMocks
  private DomainClientService domainClientService;

  /**
   * Inyección de los MOCKS y seteo del comportamiento personalizado o INICIAL.
   */
  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    // COMPORTAMIENTOS EN UN ESCENARIO SIN ERRORES
    when(clientPort.save(any(ClientDTO.class))).thenReturn(Mono.just(testDTO));
    when(clientPort.update(anyString(), any(Object.class))).thenReturn(Mono.just("Client updated successfully"));
    when(clientPort.delete(anyString())).thenReturn(Mono.just("Client deleted successfully"));
    when(clientPort.findByID(anyString())).thenReturn(Mono.just(testDTO));
    when(clientPort.findAll()).thenReturn(Flux.just(testDTO, testDTO2));
  }

  @Test
  void shouldReturnAddedClient() {
    testDTO.setDocumentNumber(clientId);
    CustomResponse<ClientDTO> expectedResponse = new CustomResponse<>(true, testDTO);

    Mono<CustomResponse> result = domainClientService.addClient(testDTO);

    StepVerifier.create(result)
        .expectNext(expectedResponse)
        .verifyComplete();
  }

  @Test
  void shouldReturnErrorWhileAdding() {
    String errorMessage = "An error has ocurred while saving the client";
    CustomError expectedError = new CustomError(errorMessage, CustomError.ErrorType.GENERIC_ERROR); // Error genérico que se menciona al inicio
    CustomResponse<CustomError> expectedResponse = new CustomResponse<>(false, expectedError); // Respuesta con error genérico
    when(clientPort.save(any(ClientDTO.class))).thenReturn(Mono.error(new RuntimeException(errorMessage))); // Comportamiento que "LANZA" el error

    Mono<CustomResponse> result = domainClientService.addClient(testDTO);

    StepVerifier.create(result)
        .expectNext(expectedResponse)
        .verifyComplete();
  }

  @Test
  void shouldReturnUpdatedClient() {
    CustomResponse<String> expectedResponse = new CustomResponse<>(true, "Client updated successfully");

    Mono<CustomResponse> result = domainClientService.updateClient(clientId, test);

    StepVerifier.create(result)
        .expectNext(expectedResponse)
        .verifyComplete();
  }

  @Test
  void shouldReturnGenericErrorWhileUpdating() {
    String errorMessage = "An error has occurred while updating the client";
    CustomError expectedError = new CustomError(errorMessage, CustomError.ErrorType.GENERIC_ERROR);
    CustomResponse<CustomError> expectedResponse = new CustomResponse<>(false, expectedError);
    when(clientPort.update(anyString(), any(Object.class))).thenReturn(Mono.error(new RuntimeException(errorMessage)));

    Mono<CustomResponse> result = domainClientService.updateClient(clientId, test);

    StepVerifier.create(result)
        .expectNext(expectedResponse)
        .verifyComplete();
  }

  @Test
  void shouldReturnStringWhileDeleting() {
    CustomResponse<String> expectedResponse = new CustomResponse<>(true, "Client deleted successfully");

    Mono<CustomResponse> result = domainClientService.deleteClient(clientId);

    StepVerifier.create(result)
        .expectNext(expectedResponse)
        .verifyComplete();
  }

  @Test
  void shouldReturnErrorWhileDeleting() {
    String errorMessage = "An error has occurred while deleting the client";
    CustomError expectedError = new CustomError(errorMessage, CustomError.ErrorType.GENERIC_ERROR);
    CustomResponse<CustomError> expectedResponse = new CustomResponse<>(false, expectedError);
    when(clientPort.delete(anyString())).thenReturn(Mono.error(new RuntimeException(errorMessage)));

    Mono<CustomResponse> result = domainClientService.deleteClient(clientId);

    StepVerifier.create(result)
        .expectNext(expectedResponse)
        .verifyComplete();
  }

  @Test
  void shouldReturnClientWhileFound() {
    testDTO.setDocumentNumber(clientId);
    CustomResponse<ClientDTO> expectedResponse = new CustomResponse<>(true, testDTO);

    Mono<CustomResponse> result = domainClientService.getClient(clientId);

    StepVerifier.create(result)
        .expectNext(expectedResponse)
        .verifyComplete();
  }

  @Test
  void shouldNotReturnWhileGetting() {
    String errorMessage = "An error has occurred while getting the client";
    CustomError expectedError = new CustomError(errorMessage, CustomError.ErrorType.GENERIC_ERROR);
    CustomResponse<CustomError> expectedResponse = new CustomResponse<>(false, expectedError);
    when(clientPort.findByID(anyString())).thenReturn(Mono.error(new NotFound(errorMessage)));

    Mono<CustomResponse> result = domainClientService.getClient(clientId);

    StepVerifier.create(result)
        .expectNext(expectedResponse)
        .verifyComplete();
  }

  @Test
  void shouldReturnAllClients() {
    List<ClientDTO> clients = Arrays.asList(testDTO, testDTO2);
    CustomResponse<Flux<ClientDTO>> expectedResponse = new CustomResponse<>(true, Flux.fromIterable(clients));
    when(clientPort.findAll()).thenReturn(expectedResponse.getData());

    Mono<CustomResponse<List<ClientDTO>>> result = domainClientService.getAllClients();

    StepVerifier.create(result)
        .expectNextMatches(response -> response.isSuccess() && response.getData() != Flux.empty())
        .verifyComplete();
  }
}
