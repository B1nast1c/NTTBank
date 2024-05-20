package com.nttdata.project.bank.account.application.controller;

import com.nttdata.project.bank.account.application.requestModels.request.BankAccountRequest;
import com.nttdata.project.bank.account.application.requestModels.response.BankAccountResponse;
import com.nttdata.project.bank.account.application.service.BankAccountExternalServiceCustom;
import com.nttdata.project.bank.account.infrastructure.mapper.GenericMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/accounts")
public class BankAccountController {

  private final BankAccountExternalServiceCustom bankAccountExternalServiceCustom;

  public BankAccountController(BankAccountExternalServiceCustom bankAccountExternalServiceCustom) {
    this.bankAccountExternalServiceCustom = bankAccountExternalServiceCustom;
  }

  @PostMapping(value = "/create")
  public ResponseEntity<BankAccountResponse> saveAccount(@Valid @RequestBody BankAccountRequest bankAccountRequest) {
    //System.out.println(GenericMapper.mapToDTO(bankAccountRequest).getId());
    bankAccountExternalServiceCustom.saveBankAccount(GenericMapper.mapToDTO(bankAccountRequest));
    return ResponseEntity.status(HttpStatus.CREATED).body(
        BankAccountResponse.builder()
            .code("200")
            .message("Successfully created client")
            .build()
    );
  }
}
