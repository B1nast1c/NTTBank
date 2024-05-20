package project.transactionsservice.infrastructure.serviceCalls.responses;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GenericResponse<T> {
  private boolean success;
  private T data;
}
