package project.domain.model.Account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import project.domain.model.AccountType;
import project.domain.model.BankProduct;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount extends BankProduct {
  @Id
  protected String accountNumber;
  protected double balance = 0.0;
  protected int transactions = 0;
  protected AccountType accountType;
}
