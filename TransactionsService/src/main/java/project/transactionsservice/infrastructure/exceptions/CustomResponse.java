package project.transactionsservice.infrastructure.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Respuesta genérica que varía su contenido: Error o Success.
 *
 * @param <T> Tipo genérico
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomResponse<T> {
  private boolean success; // Corresponde si la operación ha tenido éxito.
  private T data;
}