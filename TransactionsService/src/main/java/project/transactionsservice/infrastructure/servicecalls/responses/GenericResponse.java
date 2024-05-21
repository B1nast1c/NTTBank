package project.transactionsservice.infrastructure.servicecalls.responses;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GenericResponse {
  private boolean success;
  private Object data;
}
