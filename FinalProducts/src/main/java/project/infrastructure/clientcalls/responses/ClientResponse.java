package project.infrastructure.clientcalls.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Respuesta genérica que varía su contenido: Error o Success.
 *
 * @param <T> Tipo genérico
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientResponse {
  private boolean success;
  private Client data;
}