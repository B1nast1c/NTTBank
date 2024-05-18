package project.infrastructure.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Error personalizado lanzado dependiendo del escenario
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomResponse {
  private String message;
  private Type type;

  public enum Type {
    INVALID_TYPE, // Tipo de cliente inválido
    INVALID_DOCUMENT, // DNI/RUC inválidos
    NOT_FOUND, // Entidad no encontrada
    GENERIC_ERROR // Error genérico
  }
}