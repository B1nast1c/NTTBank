package project.domain.model.Account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "currentAccounts")
public class CurrentAccount extends BankAccount {
  @DBRef
  protected List<Titular> accountTitulars = new ArrayList<>();
  @DBRef
  protected List<LegalSigner> legalSigners = new ArrayList<>();
  private double comissionAmmount;

  public void addTitulars(List<Titular> titulars) {
    List<Titular> updatedTitulars = this.getAccountTitulars();
    updatedTitulars.addAll(titulars);
    setAccountTitulars(updatedTitulars);
  }

  public void addLegalSigners(List<LegalSigner> titulars) {
    List<LegalSigner> updatedSigners = this.getLegalSigners();
    updatedSigners.addAll(titulars);
    setLegalSigners(updatedSigners);
  }

  public void removeTitular(int titularId) {
    if (this.getAccountTitulars().size() > 1) {
      List<Titular> updatedTitulars = this.getAccountTitulars().stream().filter(item -> item.getOwnerId() != titularId)
              .collect(Collectors.toList());
      setAccountTitulars(updatedTitulars);
    }
  }

  public void removeLegalSigner(int signerId) {
    List<LegalSigner> updatedSigners = this.getLegalSigners().stream().filter(item -> item.getOwnerId() != signerId)
            .collect(Collectors.toList());
    setLegalSigners(updatedSigners);
  }
}
