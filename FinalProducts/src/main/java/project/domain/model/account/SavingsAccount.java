package project.domain.model.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * El tipo de cuenta AHORRO
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "savingsAccounts")
public class SavingsAccount extends BankAccount {
  private int movementsLimit;
}
