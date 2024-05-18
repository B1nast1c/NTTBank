package project.infrastructure.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientDTOTest {
  @Test
  void testClientDTOConstructorAndGetters() {
    String customId = "12345";
    String clientType = "PERSONAL";
    String clientName = "Miranda Flowers";
    String clientAddress = "123 Plaza de Armas";
    String clientEmail = "mirismiris@example.com";
    String clientPhone = "555-123-888";
    String documentNumber = "987654321";

    ClientDTO clientDTO = new ClientDTO(customId, clientType, clientName, clientAddress, clientEmail, clientPhone, documentNumber);

    assertNotNull(clientDTO);
    assertEquals(customId, clientDTO.getCustomId());
    assertEquals(clientType, clientDTO.getClientType());
    assertEquals(clientName, clientDTO.getClientName());
    assertEquals(clientAddress, clientDTO.getClientAddress());
    assertEquals(clientEmail, clientDTO.getClientEmail());
    assertEquals(clientPhone, clientDTO.getClientPhone());
    assertEquals(documentNumber, clientDTO.getDocumentNumber());
  }

  @Test
  void testClientDTOEmptyConstructor() {
    ClientDTO clientDTO = new ClientDTO();

    assertNull(clientDTO.getCustomId());
    assertNull(clientDTO.getClientType());
    assertNull(clientDTO.getClientName());
    assertNull(clientDTO.getClientAddress());
    assertNull(clientDTO.getClientEmail());
    assertNull(clientDTO.getClientPhone());
    assertNull(clientDTO.getDocumentNumber());
  }
}