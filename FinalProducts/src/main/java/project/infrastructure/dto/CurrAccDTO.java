package project.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * DTO Cuenta Corriente.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrAccDTO extends BankAccountDTO {
  double comissionAmmount = 0.0;
  private Set<String> accountTitulars = new HashSet<>();
  private List<LegalSignerDTO> legalSigners = new ArrayList<>();
}
