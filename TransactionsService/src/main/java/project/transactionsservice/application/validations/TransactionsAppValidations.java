package project.transactionsservice.application.validations;

import project.transactionsservice.infrastructure.dto.TransactionDTO;
import reactor.core.publisher.Mono;

public class TransactionsAppValidations {
  public Mono<TransactionDTO> validateProductNumber() {
    // Validación de la respuesta obtenida por el id del producto, ya sea para ingresar transacciones
    // O para obtener todas lsa transacciones correspondientes a cada producto bancario
    return null;
  }
}
