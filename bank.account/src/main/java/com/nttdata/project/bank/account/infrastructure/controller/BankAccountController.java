package com.nttdata.project.bank.account.infrastructure.controller;

import com.nttdata.project.bank.account.application.dto.request.BankAccountRequest;
import com.nttdata.project.bank.account.application.dto.response.BankAccountResponse;
import com.nttdata.project.bank.account.application.service.BankAccountExternalServiceCustom;
import com.nttdata.project.bank.account.domain.model.BankAccount;
import com.nttdata.project.bank.account.infrastructure.Mapper.BankAccountMapper;
import com.nttdata.project.bank.account.infrastructure.dao.repository.BankAccountRepositoryMongo;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/accounts")
public class BankAccountController {

    private final BankAccountExternalServiceCustom bankAccountExternalServiceCustom;

    private final BankAccountRepositoryMongo bankAccountRepositoryMongo;

    @PostMapping(value = "/create")
    public ResponseEntity<BankAccountResponse> saveAccount(@Valid @RequestBody BankAccountRequest bankAccountRequest) {
        //System.out.println(BankAccountMapper.mapToDTO(bankAccountRequest).getId());
        bankAccountExternalServiceCustom.saveBankAccount(BankAccountMapper.mapToDTO(bankAccountRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(
                BankAccountResponse.builder()
                        .code("200")
                        .message("Successfully created client")
                        .build()
        );
    }
}
