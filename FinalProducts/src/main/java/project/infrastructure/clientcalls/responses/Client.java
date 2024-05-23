package project.infrastructure.clientcalls.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Respuesta del servcio de clientes (DATA).
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Client {
  String customId;
  String clientType;
  String clientName;
  String clientAddress;
  String clientEmail;
  String clientPhone;
  String documentNumber;
  Boolean status = true;
}
