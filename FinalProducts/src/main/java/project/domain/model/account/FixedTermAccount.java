package project.domain.model.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Tipo de cuenta a PLAZO_FIJO.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "fixedTermAccounts")
public class FixedTermAccount extends BankAccount {
  private Date movementDate = new Date(); // Fecha del ultimo movimiento (HOY)
}
