package project.domain.validations;

import org.junit.jupiter.api.Test;
import project.domain.model.account.SavingsAccount;
import project.infrastructure.dto.BankAccountDTO;
import project.infrastructure.dto.SavingsDTO;
import project.infrastructure.exceptions.throwable.InvalidRule;
import reactor.test.StepVerifier;

class UpdateDomainValidationsTest {
  private final BankAccountDTO testDTO = new BankAccountDTO();
  private final SavingsDTO savingsTest = new SavingsDTO();
  private final UpdateDomainValidations updateDomainValidations = new UpdateDomainValidations();

  @Test
  void shouldAllowPositiveBalance() {
    testDTO.setBalance(100);

    StepVerifier.create(updateDomainValidations.validateAmmount(testDTO))
        .expectNext(testDTO)
        .verifyComplete();
  }

  @Test
  void shouldAbortNegativeBalance() {
    testDTO.setBalance(-100);

    StepVerifier.create(updateDomainValidations.validateAmmount(testDTO))
        .expectError(InvalidRule.class)
        .verify();
  }

  @Test
  void shouldAllowMovementsLowerThanLimit() {
    savingsTest.setTransactions(5);
    SavingsAccount savingsAccount = new SavingsAccount();
    savingsAccount.setMovementsLimit(10);

    StepVerifier.create(updateDomainValidations.validateSavingsAccount(savingsTest, savingsAccount))
        .expectNext(true)
        .verifyComplete();
  }

  @Test
  void shouldNotAllowMovementsHigherThanLimit() {
    savingsTest.setTransactions(15);
    SavingsAccount savingsAccount = new SavingsAccount();
    savingsAccount.setMovementsLimit(10);

    StepVerifier.create(updateDomainValidations.validateSavingsAccount(savingsTest, savingsAccount))
        .expectNext(false)
        .verifyComplete();
  }

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
