package project.domain.model.Account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "fixedTermAccounts")
public class FixedTermAccount extends BankAccount {
  private Date movementDate; // Posible edición para setear la fecha maxima de deposito, por defecto es el dia de hoy
}
