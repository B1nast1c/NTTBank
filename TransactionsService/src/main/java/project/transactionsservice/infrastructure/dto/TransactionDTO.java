package project.transactionsservice.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO del modelo {@link project.transactionsservice.domain.model.Transaction}
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO implements Serializable {
  String transactionId;
  String productNumber; // CUENTA BANCARIA O CREDITO (Incluye a las tarjetas de crédito)
  String transactionType;
  String transactionDate;
  String clientNumber = "";
  String transactionDetail = "";
  double ammount;
}