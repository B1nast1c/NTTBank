package project.domain.model.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LegalSigner {
  private String signerNumber;
  private String name;
  private String lastName;

  public String generateSignerNumber() {
    SecureRandom random = new SecureRandom();
    return "SIGNER_" + IntStream.range(0, 8)
        .mapToObj(i -> String.valueOf(random.nextInt(10)))
        .collect(Collectors.joining());
  }
}
