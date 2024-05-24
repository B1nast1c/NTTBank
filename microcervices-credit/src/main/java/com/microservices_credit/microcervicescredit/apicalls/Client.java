package com.microservices_credit.microcervicescredit.apicalls;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Respuesta del servcio de clientes (DATA).
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Client {
  String customId;
  String clientType;
  String clientName;
  String clientAddress;
  String clientEmail;
  String clientPhone;
  String documentNumber;
  Boolean status = true;
  String createdAt;
}
