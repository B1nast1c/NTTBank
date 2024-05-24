package project.transactionsservice.domain.validations;

import lombok.extern.slf4j.Slf4j;
import project.transactionsservice.domain.model.Transaction;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.exceptions.throwable.InvalidAmmount;
import project.transactionsservice.infrastructure.exceptions.throwable.InvalidTransaction;
import project.transactionsservice.infrastructure.mapper.GenericMapper;
import project.transactionsservice.infrastructure.servicecalls.responses.AccountResponse;
import project.transactionsservice.infrastructure.servicecalls.responses.CreditResponse;
import project.transactionsservice.infrastructure.servicecalls.responses.GenericResponse;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;

@Slf4j
public abstract class TransactionDomainValidations {
  private static LocalDate convertToLocalDate(Date date) {
    return Instant.ofEpochMilli(date.getTime())
        .atZone(ZoneId.systemDefault())
        .toLocalDate();
  }

  static boolean isSameYearAndMonth(String date1, Date date2) {
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    try {
      LocalDate localDate1 = LocalDate.parse(date1, dateFormatter);
      LocalDate localDate2 = convertToLocalDate(date2);
      return localDate1.getYear() == localDate2.getYear()
          && localDate1.getMonthValue() == localDate2.getMonthValue();
    } catch (DateTimeParseException e) {
      throw new InvalidTransaction("Date parsing error");
    }
  }

  public static Mono<TransactionDTO> validateFxdAccount(TransactionDTO transaction,
                                                        List<Transaction> transactions) {

    String transactionAccount = transaction.getProductNumber();
    String transactionDate = transaction.getTransactionDate();

    boolean onlyAccount = transactions
        .stream()
        .anyMatch(found -> found.getProductNumber().equals(transactionAccount)
            && isSameYearAndMonth(transactionDate, found.getTransactionDate()));

    if (!onlyAccount) {
      return Mono.just(transaction);
    }
    log.warn("Fixed account allows just a transaction per month");
    return Mono.error(new InvalidAmmount("Fixed account allows just a transaction per month"));
  }

  public static Mono<TransactionDTO> validateDeposit(TransactionDTO transaction) {
    if (transaction.getAmmount() <= 100000) {
      return Mono.just(transaction);
    }
    log.warn("The deposit ammount is too big");
    return Mono.error(new InvalidAmmount("The deposit ammount is too big"));
  }

  public static Mono<TransactionDTO> validateWithdrawal(TransactionDTO transaction, AccountResponse account) {
    if (account.getBalance() >= transaction.getAmmount()) {
      return Mono.just(transaction);
    }
    log.warn("Insufficient funds for withdrawal");
    return Mono.error(new InvalidAmmount("Insufficient funds for withdrawal"));
  }

  public static Mono<TransactionDTO> validateCreditPayment(TransactionDTO transaction, CreditResponse credit) {
    if (credit.getAmmount() == transaction.getAmmount()) {
      return Mono.just(transaction);
    } else {
      return Mono.error(new InvalidAmmount("The credit payment ammount does not match the required ammount"));
    }
  }

  public static Mono<TransactionDTO> validateCreditCardCharge(TransactionDTO transaction, CreditResponse creditCard) {
    if (creditCard.getAmmount() >= transaction.getAmmount()) {
      TransactionDTO mappedTransaction = GenericMapper.mapToAny(creditCard, TransactionDTO.class);
      return Mono.just(mappedTransaction);
    }
    return Mono.error(new InvalidAmmount("The charge exceeds the available credit limit"));
  }

  protected abstract Mono<TransactionDTO> validateTransaction(TransactionDTO transaction, GenericResponse serviceResponse);
}
