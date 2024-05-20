package project.application.service;

import project.infrastructure.dto.BankAccountDTO;
import project.infrastructure.dto.CurrAccDTO;
import project.infrastructure.dto.LegalSignerDTO;
import project.infrastructure.responses.CustomResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BankAccountService {
  Mono<CustomResponse<Object>> createBankAccount(Object bankAccountDTO);

  Mono<CustomResponse<Object>> getBankAccount(String accountNumber);

  Mono<CustomResponse<Object>> updateBankAccount(String accountNumber,
                                                 BankAccountDTO bankAccountDTO);

  Mono<CustomResponse<Object>> deleteBankAccount(String accountNumber);

  Mono<CustomResponse<List<Object>>> getBankAccounts();

  Mono<CustomResponse<Object>> getAllBankAccountsByClientId(String clientId);

  Mono<CustomResponse<CurrAccDTO>> addTitularsToAccount(String accountNumber,
                                                        List<String> titulars);

  Mono<CustomResponse<CurrAccDTO>> addLegalSignersToAccount(String accountNumber,
                                                            List<LegalSignerDTO> legalSignersDTO);

  Mono<CustomResponse<CurrAccDTO>> removeTitularfromAccount(String accountNumber,
                                                            String titularId);

  Mono<CustomResponse<CurrAccDTO>> removeLegalSignerfromAccount(String accountNumber,
                                                                String signerId);

  Mono<CustomResponse<Object>> getAccountBalance(String accountNumber);
}
