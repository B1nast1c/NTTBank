package project.transactionsservice.infrastructure.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO del modelo {@link project.transactionsservice.domain.model.Transaction}
 */
@Value
public class TransactionDTO implements Serializable {
  String transactionId;
  String productNumber; // CUENTA BANCARIA O CREDITO (Incluye a las tarjetas de crédito)
  String transactionType;
  double amount;
  String transactionDate;
}