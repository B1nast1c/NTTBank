package project.infrastructure.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LegalSignerDTO implements Serializable {
  int ownerId;
  String name;
  String lastName;
}