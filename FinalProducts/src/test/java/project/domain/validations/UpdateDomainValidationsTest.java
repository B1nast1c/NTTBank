package project.domain.validations;

import org.junit.jupiter.api.Test;
import project.domain.model.account.SavingsAccount;
import project.infrastructure.dto.BankAccountDTO;
import project.infrastructure.dto.SavingsDTO;
import project.infrastructure.exceptions.throwable.InvalidRule;
import reactor.test.StepVerifier;

/**
 * Clase de prueba para las validaciones de dominio al actualizar cuentas bancarias.
 */
class UpdateDomainValidationsTest {
  private final BankAccountDTO testDTO = new BankAccountDTO();
  private final SavingsDTO savingsTest = new SavingsDTO();
  private final UpdateDomainValidations updateDomainValidations = new UpdateDomainValidations();

  /**
   * Prueba que verifica que se permita un balance positivo.
   */
  @Test
  void shouldAllowPositiveBalance() {
    testDTO.setBalance(100);

    StepVerifier.create(updateDomainValidations.validateAmmount(testDTO))
        .expectNext(testDTO)
        .verifyComplete();
  }

  /**
   * Prueba que verifica que se rechace un balance negativo.
   */
  @Test
  void shouldAbortNegativeBalance() {
    testDTO.setBalance(-100);

    StepVerifier.create(updateDomainValidations.validateAmmount(testDTO))
        .expectError(InvalidRule.class)
        .verify();
  }

  /**
   * Prueba que verifica que se permitan movimientos menores al límite.
   */
  @Test
  void shouldAllowMovementsLowerThanLimit() {
    savingsTest.setTransactions(5);
    SavingsAccount savingsAccount = new SavingsAccount();
    savingsAccount.setMovementsLimit(10);

    StepVerifier.create(updateDomainValidations.validateSavingsAccount(savingsTest, savingsAccount))
        .expectNext(true)
        .verifyComplete();
  }

  /**
   * Prueba que verifica que no se permitan movimientos mayores al límite.
   */
  @Test
  void shouldNotAllowMovementsHigherThanLimit() {
    savingsTest.setTransactions(15);
    SavingsAccount savingsAccount = new SavingsAccount();
    savingsAccount.setMovementsLimit(10);

    StepVerifier.create(updateDomainValidations.validateSavingsAccount(savingsTest, savingsAccount))
        .expectNext(false)
        .verifyComplete();
  }

  /**
   * Prueba que verifica que no se permitan movimientos negativos.
   */
  @Test
  void shouldNotAllowNegativeMovements() {
    savingsTest.setTransactions(-5);
    SavingsAccount savingsAccount = new SavingsAccount();
    savingsAccount.setMovementsLimit(10);

    StepVerifier.create(updateDomainValidations.validateSavingsAccount(savingsTest, savingsAccount))
        .expectNext(false)
        .verifyComplete();
  }
}