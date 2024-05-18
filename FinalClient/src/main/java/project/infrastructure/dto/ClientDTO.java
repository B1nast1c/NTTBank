package project.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO de {@link project.domain.model.Client}
 */
@Data
@NonNull
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO implements Serializable {
  String customId;
  String clientType;
  String clientName;
  String clientAddress;
  String clientEmail;
  String clientPhone;
  String documentNumber;
  Boolean status = true;
  String createdAt = new Date().toString();

  public ClientDTO(String customId, String clientType, String clientName, String clientAddress, String clientEmail, String clientPhone, String documentNumber) {
    this.customId = customId;
    this.clientType = clientType;
    this.clientName = clientName;
    this.clientAddress = clientAddress;
    this.clientEmail = clientEmail;
    this.clientPhone = clientPhone;
    this.documentNumber = documentNumber;
  }
}