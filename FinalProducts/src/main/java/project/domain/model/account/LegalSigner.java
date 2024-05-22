package project.domain.model.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * El tipo LegalSigner (FIRMANTE LEGAL).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LegalSigner {
  private String signerNumber;
  private String name;
  private String lastName;

  /**
   * Generar número de firmante.
   *
   * @return El número del firmante generado
   */
  public String generateSignerNumber() {
    SecureRandom random = new SecureRandom();
    return "SIGNER_" + IntStream.range(0, 8)
        .mapToObj(i -> String.valueOf(random.nextInt(10)))
        .collect(Collectors.joining());
  }
}
