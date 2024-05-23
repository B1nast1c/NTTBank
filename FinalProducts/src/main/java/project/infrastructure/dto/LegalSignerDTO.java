package project.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO de Firmantes Legales.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LegalSignerDTO implements Serializable {
  String signerNumber;
  String name;
  String lastName;
}