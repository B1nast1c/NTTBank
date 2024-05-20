package project.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrAccDTO extends BankAccountDTO {
  double comissionAmmount = 0.0;
  private Set<String> accountTitulars = new HashSet<>();
  private List<LegalSignerDTO> legalSigners = new ArrayList<>();
}
