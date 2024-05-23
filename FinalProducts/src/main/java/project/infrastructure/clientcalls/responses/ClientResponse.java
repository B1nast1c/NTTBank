package project.infrastructure.clientcalls.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Respuesta genérica que varía su contenido: Error o Success.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse {
  private boolean success;
  private Client data;
}