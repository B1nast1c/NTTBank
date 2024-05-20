package project.domain.model.account;

import lombok.*;
import org.springframework.data.annotation.Id;
import project.domain.model.BankProduct;

import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class BankAccount extends BankProduct {
  protected String accountNumber;
  protected double balance = 0.0;
  protected int transactions = 0;
  protected String accountType;

  @Id
  private String id;

  public String generateAccountNumber() {
    SecureRandom random = new SecureRandom();
    return IntStream.range(0, 16)
        .mapToObj(i -> String.valueOf(random.nextInt(10)))
        .collect(Collectors.joining());
  }
}
