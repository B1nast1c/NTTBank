package project.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LegalSignerDTO implements Serializable {
  String signerNumber;
  String name;
  String lastName;
}