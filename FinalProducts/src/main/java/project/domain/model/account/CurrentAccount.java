package project.domain.model.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * La clase Cuenta Corriente.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "currentAccounts")
public class CurrentAccount extends BankAccount {
  protected List<LegalSigner> legalSigners = new ArrayList<>();
  protected Set<String> accountTitulars = new HashSet<>();
  private double comissionAmmount;
}
