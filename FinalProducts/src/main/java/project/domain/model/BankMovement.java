package project.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import project.domain.model.Account.BankAccount;
import project.domain.model.Credits.Credit;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "bankMovements")
public class BankMovement {
  @Id
  private String movementId;

  @DBRef
  private Credit credit;

  @DBRef
  private BankAccount bankAccount;

  private MovementType movementType;
  private double movementAmmount;
  private Date movementDate;
}
