package project.domain.validations;

import org.springframework.stereotype.Component;
import project.domain.model.account.SavingsAccount;
import project.infrastructure.dto.BankAccountDTO;
import project.infrastructure.dto.SavingsDTO;
import project.infrastructure.exceptions.throwable.InvalidRule;
import project.infrastructure.mapper.GenericMapper;
import reactor.core.publisher.Mono;

@Component
public class UpdateDomainValidations {
  public Mono<Object> validateAmmount(Object updatedAccount) {
    BankAccountDTO updatedDTO = GenericMapper.mapToSpecificClass(updatedAccount, BankAccountDTO.class);
    if (updatedDTO.getBalance() < 0) {
      return Mono.error(new InvalidRule("Can't update account with negative balance"));
    }
    return Mono.just(updatedAccount);
  }

  public Mono<Boolean> validateSavingsAccount(SavingsDTO accountDTO, SavingsAccount savingsAccount) {
    return Mono.just(
        !(accountDTO.getTransactions() > savingsAccount.getMovementsLimit()
            || accountDTO.getTransactions() < 0));
  }
}
