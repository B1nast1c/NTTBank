package project.infrastructure.exceptions;

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
public class CustomResponse<T> {
  private boolean success; // Corresponde si la operación ha tenido éxito.
  private T data;
}