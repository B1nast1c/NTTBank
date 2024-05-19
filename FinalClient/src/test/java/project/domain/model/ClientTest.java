package project.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {
  @Test
  void testClientEmptyConstructor() {
    Client client = new Client();

    assertNull(client.getCustomId());
    assertNull(client.getClientType());
    assertEquals("", client.getClientEmail());
    assertEquals("", client.getClientPhone());
    assertTrue(client.getStatus());
    assertNotNull(client.getCreatedAt());
    assertEquals("", client.getClientName());
    assertEquals("", client.getClientAddress());
    assertNull(client.getDocumentNumber());
  }
}