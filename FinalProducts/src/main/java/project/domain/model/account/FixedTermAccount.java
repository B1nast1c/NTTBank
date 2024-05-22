package project.domain.model.account;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Tipo de cuenta a PLAZO_FIJO.
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "fixedTermAccounts")
public class FixedTermAccount extends BankAccount {
  private Date movementDate = new Date(); // Fecha del ultimo movimiento (HOY)
}
