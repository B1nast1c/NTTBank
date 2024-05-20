package com.nttdata.project.bank.account.application.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BankAccountResponse {
    private String message;
    private String code;
}
