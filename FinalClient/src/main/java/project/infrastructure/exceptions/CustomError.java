package project.infrastructure.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Error personalizado lanzado dependiendo del escenario
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomError {
  private String message;
  private Type type;

  public enum Type {
    INVALID_TYPE, // Tipo de cliente inválido
    INVALID_DOCUMENT, // DNI/RUC inválidos
    NOT_FOUND, // Entidad no encontrada
    GENERIC_ERROR // Error genérico
  }
}