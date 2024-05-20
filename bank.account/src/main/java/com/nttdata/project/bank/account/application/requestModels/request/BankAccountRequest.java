package com.nttdata.project.bank.account.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountRequest {

    private String id;
    @NotBlank(message = "Este campo no puede estar vacio")
    @Pattern(regexp = "^\\d{16}$", message = "Número de cuenta no valido")
    private String numberAccount;

    @NotBlank(message = "Este campo no puede estar vacio")
    //@Pattern(regexp = "\"^(Cuenta Corriente|Plazo Fijo|Ahorro)$\", message = \"Tipo de cuenta no válido\"")
    private String type;
    private String balance;
    private String numberTransfers;
    private String numberInterbank;
    private String commissionAmount;
    private String headlines;
    private String signatories;
    private String monthlyMovements;
}
