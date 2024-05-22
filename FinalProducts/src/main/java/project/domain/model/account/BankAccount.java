package project.domain.model.account;

import lombok.*;
import org.springframework.data.annotation.Id;
import project.domain.model.BankProduct;

import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * La clase Cuenta Bancaria.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class BankAccount extends BankProduct {
  protected String accountNumber;
  protected double balance = 0.0;
  protected int transactions = 0;
  protected String accountType;

  @Id
  private String id;

  /**
   * Genera un número de cuenta.
   *
   * @return el número de cuenta como una cadena de texto
   */
  public String generateAccountNumber() {
    // Crea una instancia de SecureRandom para generar números aleatorios seguros
    SecureRandom random = new SecureRandom();
    // Genera una secuencia de 16 números aleatorios y los une en una cadena
    return IntStream.range(0, 16)
        .mapToObj(i -> String.valueOf(random.nextInt(10))) // Convierte cada número a una cadena
        .collect(Collectors.joining()); // Une todas las cadenas en una sola
  }
}