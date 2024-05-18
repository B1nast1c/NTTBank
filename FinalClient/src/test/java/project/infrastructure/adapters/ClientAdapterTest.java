package project.infrastructure.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import project.application.validations.ClientAppValidations;
import project.domain.model.Client;
import project.domain.model.ClientType;
import project.domain.validations.ClientDomainValidations;
import project.infrastructure.adapters.mongoRepos.ClientRepository;
import project.infrastructure.dto.ClientDTO;
import project.infrastructure.exceptions.CustomError;
import project.infrastructure.exceptions.throwable.NotFound;
import project.infrastructure.mapper.GenericMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

// TODO -> Mejorar la cobertura de las pruebas de la implementación del repositorio
// TODO -> Contemplar las posibles BRANCHES dentro del código
// Bastantes branches, puesto que existen gran cantidad de excepciones (CAMINOS) + cobertura de líneas de código

import static org.mockito.Mockito.*;

public class ClientAdapterTest {

  @Mock
  private ClientRepository clientRepository;

  @Mock
  private ClientDomainValidations clientValidations;

  @Mock
  private ClientAppValidations appValidations;

  @InjectMocks
  private ClientAdapter clientAdapter;

  private Client client1;
  private Client client2;
  private ClientDTO clientDTO1;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);

    client1 = new Client("1",
        ClientType.PERSONAL,
        "john@example.com",
        "123-456-789",
        true,
        "2023-01-01",
        "Luciano Guerra",
        "Random location",
        "123456789");

    client2 = new Client("2",
        ClientType.EMPRESARIAL,
        "john@example.com",
        "123-456-789",
        true,
        "2023-01-01",
        "Mariana Guevara",
        "Random Direction",
        "987654321");
    clientDTO1 = GenericMapper.mapToDto(client1);

    // Comportamientos predefinidos (MOCKS)

    when(clientRepository.insert(Mockito.any(Client.class))).thenReturn(Mono.just(client1));
    when(clientRepository.findByDocumentNumber(client1.getDocumentNumber())).thenReturn(Mono.just(client1));
    when(clientRepository.findAll()).thenReturn(Flux.just(client1, client2));
    when(clientRepository.findById(Mockito.any(String.class))).thenReturn(Mono.just(client1));
    when(clientRepository.deleteById(Mockito.any(String.class))).thenReturn(Mono.empty());
    when(appValidations.validateDocumentNumber(any(ClientDTO.class))).thenReturn(Mono.just(clientDTO1));
    when(clientValidations.validateClientType(any(ClientDTO.class))).thenReturn(Mono.just(clientDTO1));
  }

  @Test
  public void shouldFindById() {
    Mono<ClientDTO> result = clientAdapter.findByID(client1.getDocumentNumber()).map(GenericMapper::mapToDto);

    StepVerifier.create(result)
        .expectNextMatches(foundClient -> foundClient
            .getDocumentNumber()
            .equals(client1.getDocumentNumber()))
        .verifyComplete();

    verify(clientRepository, times(1)).findByDocumentNumber(client1.getDocumentNumber());
  }

  @Test
  public void shouldNotFindNull() {
    String documentNumber = "non-existent";
    when(clientRepository.findByDocumentNumber(documentNumber)).thenReturn(Mono.empty());
    Mono<?> result = clientAdapter.findByID(documentNumber);

    StepVerifier.create(result).verifyError(NotFound.class);

    verify(clientRepository, times(1)).findByDocumentNumber(documentNumber);
  }

  @Test
  public void shouldSaveClient() {
    Mono<ClientDTO> result = clientAdapter.save(clientDTO1).map(GenericMapper::mapToDto);

    StepVerifier
        .create(result)
        .expectNextMatches(savedClient -> "Luciano Guerra".equals(savedClient.getClientName()))
        .verifyComplete();

    verify(clientRepository, times(1)).insert(any(Client.class));
  }

  @Test
  public void shouldNotSaveClientByDocumentNumber() {
    clientDTO1.setDocumentNumber("");
    Mono<?> result = clientAdapter.save(clientDTO1);

    StepVerifier.create(result)
        .expectNextMatches(response -> {
          if (response instanceof CustomError) {
            CustomError error = (CustomError) response;
            return error.getErrorMessage().equals("El documento del cliente no puede estar vacío") &&
                error.getErrorType() == CustomError.ErrorType.INVALID_DOCUMENT;
          }
          return false;
        })
        .verifyComplete();

    verify(clientRepository, never()).insert(any(Client.class));
  }

  @Test
  public void shouldNotSaveClientWithDuplicateDocumentNumber() {
    // Preparar datos de entrada
    String documentNumber = "123456789";
    clientDTO1.setDocumentNumber(documentNumber);

    // Simular que ya existe un cliente con el mismo número de documento en la base
    // de datos
    when(clientRepository.findByDocumentNumber(documentNumber)).thenReturn(Mono.just(client1));

    // Ejecutar el método a probar
    Mono<?> result = clientAdapter.save(clientDTO1);

    // Verificar el resultado esperado
    StepVerifier.create(result)
        .expectNextMatches(response -> {
          return false;
        })
        .verifyComplete();

    // Verificar que no se llamó al método insert del repositorio mock
    verify(clientRepository, never()).insert(any(Client.class));
  }

  @Test
  public void shouldNotSaveByClient() {
    when(clientRepository.insert(any(Client.class))).thenReturn(Mono.error(new RuntimeException("Database error")));
    clientDTO1.setClientType(ClientType.PERSONAL.name());
    clientDTO1.setDocumentNumber("123456789");
    Mono<?> result = clientAdapter.save(clientDTO1);

    StepVerifier.create(result)
        .expectNextMatches(response -> {
          if (response instanceof CustomError) {
            CustomError error = (CustomError) response;
            return error.getErrorMessage().equals("Algunos campos son incorrectos o faltan") &&
                error.getErrorType() == CustomError.ErrorType.GENERIC_ERROR;
          }
          return false;
        })
        .verifyComplete();

    verify(clientRepository, times(1)).insert(any(Client.class));
  }

  @Test
  public void shouldNotSaveByClientType() {
    clientDTO1.setClientType("Invalid ErrorType");
    clientDTO1.setDocumentNumber("123456789");
    Mono<?> result = clientAdapter.save(clientDTO1);

    StepVerifier.create(result)
        .expectNextMatches(response -> {
          if (response instanceof CustomError) {
            CustomError error = (CustomError) response;
            return error.getErrorMessage().equals("El tipo de cliente debe ser PERSONAL o EMPRESARIAL") &&
                error.getErrorType() == CustomError.ErrorType.INVALID_TYPE;
          }
          return false;
        })
        .verifyComplete();

    verify(clientRepository, never()).insert(any(Client.class));
  }

  @Test
  public void shouldFindAll() {
    Flux<?> result = clientAdapter.findAll();

    StepVerifier.create(result)
        .expectNextCount(2)
        .verifyComplete();

    verify(clientRepository, times(1)).findAll();
  }

  @Test
  public void shouldNotDeleteClient() {
    String wrongId = "999";
    when(clientRepository.findById("999")).thenReturn(Mono.empty());
    Mono<?> result = clientAdapter.delete(wrongId);

    StepVerifier.create(result).verifyComplete();

    verify(clientRepository, times(1)).findById(wrongId);
    verify(clientRepository, never()).deleteById(wrongId);
  }

  @Test
  public void shouldDeleteClient() {
    String clientId = clientDTO1.getCustomId();
    Mono<String> result = clientAdapter.delete(clientId);

    StepVerifier.create(result)
        .expectNext("Cliente eliminado correctamente")
        .verifyComplete();

    verify(clientRepository, times(1)).findById(clientId);
    verify(clientRepository, times(1)).deleteById(clientId);
  }

  @Test
  public void shouldNotUpdateByClient() {
    String wrongId = "999";
    when(clientRepository.existsByCustomId(wrongId)).thenReturn(Mono.just(false));
    clientDTO1.setClientName("John Smith");
    clientDTO1.setClientAddress("Updated Address");
    Mono<?> result = clientAdapter.update(wrongId, clientDTO1);

    StepVerifier.create(result).verifyError(NotFound.class);

    verify(clientRepository, times(1)).existsByCustomId(wrongId);
    verify(clientRepository, never()).findById(wrongId);
    verify(clientRepository, never()).save(any());
  }

  @Test
  public void shouldNotUpdateGenericError() {
    String wrongId = "1";
    ClientDTO updatedClientDTO = new ClientDTO();
    updatedClientDTO.setClientName("John Smith");
    updatedClientDTO.setClientAddress("Updated Address");
    when(clientRepository.existsByCustomId(wrongId)).thenReturn(Mono.just(true));
    when(clientRepository.findById(wrongId)).thenReturn(Mono.empty());
    when(clientRepository.save(any())).thenReturn(Mono.error(new RuntimeException("Error al persistir el cliente")));
    Mono<?> result = clientAdapter.update(wrongId, updatedClientDTO);

    StepVerifier.create(result).verifyComplete();

    verify(clientRepository, times(1)).existsByCustomId(wrongId);
    verify(clientRepository, times(1)).findById(wrongId);
  }

  @Test
  public void shouldNotUpdateByRequiredFields() {
    String clientId = "1";
    ClientDTO updatedClientDTO = new ClientDTO();
    when(clientRepository.existsByCustomId(clientId)).thenReturn(Mono.just(true));
    Mono<?> result = clientAdapter.update(clientId, updatedClientDTO);

    StepVerifier.create(result).verifyError(NullPointerException.class);

    verify(clientRepository, times(1)).existsByCustomId(clientId);
    verify(clientRepository, never()).save(any());
  }

  /*
   * @Test
   * public void shouldUpdateClient() {
   * String clientId = "1";
   * clientDTO1.setClientName("John Smith");
   * clientDTO1.setClientAddress("Updated Address");
   * 
   * when(clientRepository.existsByCustomId(clientId)).thenReturn(Mono.just(true))
   * ;
   * when(clientRepository.save(any(Client.class))).thenReturn(Mono.just(client1))
   * ;
   * 
   * Mono<String> result = clientAdapter.update(clientId, clientDTO1);
   * 
   * StepVerifier.create(result)
   * .expectNext("Cliente actualizado correctamente")
   * .verifyComplete();
   * }
   */
}
