package project.domain.model.Account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Owner {
  @Id
  protected int ownerId;

  protected String name;
  protected String lastName;
}
