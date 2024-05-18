package infrastructure.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import project.domain.model.Client;
import project.domain.model.ClientType;
import project.infrastructure.dto.ClientDTO;
import project.infrastructure.mapper.GenericMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GenericMapperTest {
  @Test
  void testMapToEntity() {
    // Arrange
    ClientDTO clientDTO = new ClientDTO("1", "PERSONAL", "John Doe", "123 Main St", "john@example.com", "123456789", "1234567890");
    Client expectedClient = new Client();
    expectedClient.setCustomId("1");
    expectedClient.setClientType(ClientType.PERSONAL);
    expectedClient.setClientName("John Doe");
    expectedClient.setClientAddress("123 Main St");
    expectedClient.setClientEmail("john@example.com");
    expectedClient.setClientPhone("123456789");
    expectedClient.setDocumentNumber("1234567890");

    ModelMapper modelMapper = Mockito.mock(ModelMapper.class);
    when(modelMapper.map(any(), any())).thenReturn(expectedClient);
    Client actualClient = GenericMapper.mapToEntity(clientDTO);
    Assertions.assertEquals(expectedClient, actualClient);
  }

  @Test
  void testMapToDto() {
    Client client = new Client();
    client.setCustomId("1");
    client.setClientType(ClientType.EMPRESARIAL);
    client.setClientName("John Doe");
    client.setClientAddress("123 Main St");
    client.setClientEmail("john@example.com");
    client.setClientPhone("123456789");
    client.setDocumentNumber("1234567890");
    ClientDTO expectedClientDTO = new ClientDTO("1", "EMPRESARIAL", "John Doe", "123 Main St", "john@example.com", "123456789", "1234567890");

    ModelMapper modelMapper = Mockito.mock(ModelMapper.class);
    when(modelMapper.map(any(), any())).thenReturn(expectedClientDTO);
    ClientDTO actualClientDTO = GenericMapper.mapToDto(client);
    Assertions.assertEquals(expectedClientDTO, actualClientDTO);
  }
}
