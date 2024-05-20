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
  private String transactionId;

  @NonNull
  private String productNumber; // Número de la cuenta bancaria o del crédito

  private TransactionType transactionType;
  private double amount = 0.0;
  private Date transactionDate = new Date();
  private String transactionDetail;
}
