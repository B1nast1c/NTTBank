package project.domain.model.Credits;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import project.domain.model.BankProduct;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "credits")
public class Credit extends BankProduct {
  @Id
  private String creditId;

  private double creditAmmount = 0.0;
}
