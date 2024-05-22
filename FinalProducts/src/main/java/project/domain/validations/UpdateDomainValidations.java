package project.domain.validations;

import org.springframework.stereotype.Component;
import project.domain.model.account.SavingsAccount;
import project.infrastructure.dto.BankAccountDTO;
import project.infrastructure.dto.SavingsDTO;
import project.infrastructure.exceptions.throwable.InvalidRule;
import project.infrastructure.mapper.GenericMapper;
import reactor.core.publisher.Mono;

/**
 * La clase UpdateDomainValidations proporciona métodos para validar la actualización de cuentas.
 */
@Component
public class UpdateDomainValidations {

  /**
   * Valida que el monto de la cuenta no sea negativo.
   *
   * @param updatedAccount La cuenta actualizada.
   * @return Un Mono que indica si la validación fue exitosa.
   */
  public Mono<Object> validateAmmount(Object updatedAccount) {
    BankAccountDTO updatedDTO = GenericMapper.mapToSpecificClass(updatedAccount, BankAccountDTO.class);
    if (updatedDTO.getBalance() < 0) {
      return Mono.error(new InvalidRule("No se puede actualizar la cuenta con saldo negativo"));
    }
    return Mono.just(updatedAccount);
  }

  /**
   * Valida el límite de movimientos de una cuenta de ahorros.
   *
   * @param accountDTO     El DTO de la cuenta.
   * @param savingsAccount La cuenta de ahorros.
   * @return Un Mono que indica si la validación fue exitosa.
   */
  public Mono<Boolean> validateSavingsAccount(SavingsDTO accountDTO, SavingsAccount savingsAccount) {
    return Mono.just(
        !(accountDTO.getTransactions() > savingsAccount.getMovementsLimit()
            || accountDTO.getTransactions() < 0));
  }
}