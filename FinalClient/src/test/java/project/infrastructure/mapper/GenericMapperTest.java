package project.infrastructure.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import project.domain.model.Client;
import project.domain.model.ClientType;
import project.infrastructure.dto.ClientDTO;
import project.infrastructure.exceptions.throwable.EmptyAttributes;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Pruebas del mapper, solamente eso XD, el coverage de este elemento es de 77%
 * Según IntelliJ
 */
class GenericMapperTest {

  ClientDTO testDto = new ClientDTO();
  Client testClient = new Client();

  @Mock
  ModelMapper modelMapper;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);

    testDto = new ClientDTO("1",
        "PERSONAL",
        "John Doe",
        "123 Main St",
        "john@example.com",
        "123456789",
        "1234567890",
        true,
        "01-01-1111");
    testClient.setClientType(ClientType.PERSONAL);
    testClient.setCustomId(testDto.getCustomId());
    testClient.setClientName(testDto.getClientName());
    testClient.setClientAddress(testDto.getClientAddress());
    testClient.setClientPhone(testDto.getClientPhone());
    testClient.setClientEmail(testDto.getClientEmail());
    testClient.setDocumentNumber(testDto.getDocumentNumber());
    testClient.setStatus(testDto.getStatus());
    testClient.setCreatedAt(testDto.getCreatedAt());
  }

  @Test
  void shouldMapClientDTOToClient() {
    when(modelMapper.map(any(), any())).thenReturn(testClient);
    Client actualClient = GenericMapper.mapToEntity(testDto);
    Assertions.assertEquals(testClient, actualClient);
  }

  @Test
  void shouldMapClientToClientDTO() {
    when(modelMapper.map(any(), any())).thenReturn(testDto);
    ClientDTO actualClientDTO = GenericMapper.mapToDto(testClient);
    Assertions.assertEquals(testDto, actualClientDTO);
  }

  @Test
  void shouldNotMapClientDTOToClient() {
    testDto.setClientName(null);

    when(modelMapper.map(any(), any())).thenReturn(testClient);

    Assertions.assertThrows(EmptyAttributes.class, () -> {
      GenericMapper.mapToEntity(testDto);
    });
  }
}
