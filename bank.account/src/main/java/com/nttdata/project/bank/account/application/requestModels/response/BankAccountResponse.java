package com.nttdata.project.bank.account.application.requestModels.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BankAccountResponse<T> { // Posibilidad de mandar respuesta personalizada
  private String message;
  private T code;
}
