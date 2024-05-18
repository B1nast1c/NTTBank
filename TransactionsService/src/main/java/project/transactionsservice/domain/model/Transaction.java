package project.transactionsservice.domain.model;

import com.mongodb.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "bankTransactions")
public class Transaction {
  @Id
  protected String transactionId;

  @NonNull
  protected String productNumber; // Número de la cuenta bancaria o del crédito

  protected TransactionType transactionType;
  protected double amount = 0.0;
  protected Date transactionDate = new Date();
}
