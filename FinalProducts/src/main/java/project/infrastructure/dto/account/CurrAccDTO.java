package project.infrastructure.dto.account;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CurrAccDTO extends BankAccountDTO {
  List<TitularDTO> accountTitulars = new ArrayList<>();
  List<LegalSignerDTO> legalSigners = new ArrayList<>();
  double comissionAmmount = 0.0;
}
