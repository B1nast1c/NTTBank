package project.domain.model.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;
import java.util.stream.Collectors;

/**
 * La clase Cuenta Corriente.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "currentAccounts")
public class CurrentAccount extends BankAccount {
  protected List<LegalSigner> legalSigners = new ArrayList<>();
  protected Set<String> accountTitulars = new HashSet<>();
  private double comissionAmmount;

  /**
   * Agregar titulares.
   *
   * @param titulars los titulares
   */
  public void addTitulars(List<String> titulars) {
    Set<String> updatedTitulars = this.getAccountTitulars();
    updatedTitulars.addAll(titulars);
    setAccountTitulars(updatedTitulars);
  }

  /**
   * Agregar firmantes legales.
   *
   * @param titulars los firmantes legales
   */
  public void addLegalSigners(List<LegalSigner> titulars) {
    List<LegalSigner> updatedSigners = this.getLegalSigners();
    updatedSigners.addAll(titulars);
    setLegalSigners(updatedSigners);
  }

  /**
   * Eliminar titular.
   *
   * @param titularId el ID del titular
   */
  public void removeTitular(String titularId) {
    if (this.getAccountTitulars().size() > 1) {
      Set<String> updatedTitulars = this.getAccountTitulars().stream()
          .filter(item -> !Objects.equals(item, titularId))
          .collect(Collectors.toSet());
      setAccountTitulars(updatedTitulars);
    }
  }

  /**
   * Eliminar firmante legal.
   *
   * @param signerId el ID del firmante legal
   */
  public void removeLegalSigner(String signerId) {
    List<LegalSigner> updatedSigners = this.getLegalSigners().stream()
        .filter(item -> !Objects.equals(item.getSignerNumber(), signerId))
        .collect(Collectors.toList());
    setLegalSigners(updatedSigners);
  }
}
