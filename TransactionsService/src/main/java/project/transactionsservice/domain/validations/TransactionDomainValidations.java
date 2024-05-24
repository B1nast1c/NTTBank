package project.transactionsservice.domain.validations;

import lombok.extern.slf4j.Slf4j;
import project.transactionsservice.domain.model.Transaction;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.exceptions.throwable.InvalidAmmount;
import project.transactionsservice.infrastructure.exceptions.throwable.InvalidTransaction;
import project.transactionsservice.infrastructure.mapper.GenericMapper;
import project.transactionsservice.infrastructure.servicecalls.responses.AccountResponse;
import project.transactionsservice.infrastructure.servicecalls.responses.CreditResponse;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;

/**
 * Clase abstracta para validar transacciones de dominio.
 */
@Slf4j
public class TransactionDomainValidations {

  private TransactionDomainValidations() {
    log.info("Private constructor");
  }

  /**
   * Convierte una fecha a LocalDate.
   *
   * @param date la fecha a convertir
   * @return la fecha convertida a LocalDate
   */
  private static LocalDate convertToLocalDate(Date date) {
    return Instant.ofEpochMilli(date.getTime())
        .atZone(ZoneId.systemDefault())
        .toLocalDate();
  }

  /**
   * Verifica si dos fechas son del mismo año y mes.
   *
   * @param date1 la primera fecha en formato de cadena
   * @param date2 la segunda fecha en formato Date
   * @return verdadero si las fechas son del mismo año y mes, falso de lo contrario
   * @throws InvalidTransaction si ocurre un error al parsear la fecha
   */
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

  /**
   * Valida que una cuenta fija solo permita una transacción por mes.
   *
   * @param transaction  la transacción a validar
   * @param transactions la lista de transacciones existentes
   * @return la transacción validada si es válida, o un error si no lo es
   */
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
    log.warn("Fixed account only allows one transaction per month");
    return Mono.error(new InvalidAmmount("Fixed account only allows one transaction per month"));
  }

  /**
   * Valida el monto de un depósito.
   *
   * @param transaction la transacción a validar
   * @return la transacción validada si es válida, o un error si no lo es
   */
  public static Mono<TransactionDTO> validateDeposit(TransactionDTO transaction) {
    if (transaction.getAmmount() <= 100000) {
      return Mono.just(transaction);
    }
    log.warn("Deposit amount is too large");
    return Mono.error(new InvalidAmmount("Deposit amount is too large"));
  }

  /**
   * Valida un retiro en función del saldo de la cuenta.
   *
   * @param transaction la transacción a validar
   * @param account     la respuesta del servicio que contiene el saldo de la cuenta
   * @return la transacción validada si es válida, o un error si no lo es
   */
  public static Mono<TransactionDTO> validateWithdrawal(TransactionDTO transaction, AccountResponse account) {
    if (account.getBalance() >= transaction.getAmmount()) {
      return Mono.just(transaction);
    }
    log.warn("Insufficient funds for withdrawal");
    return Mono.error(new InvalidAmmount("Insufficient funds for withdrawal"));
  }

  /**
   * Valida el pago de un crédito.
   *
   * @param transaction la transacción a validar
   * @param credit      la respuesta del servicio que contiene la información del crédito
   * @return la transacción validada si es válida, o un error si no lo es
   */
  public static Mono<TransactionDTO> validateCreditPayment(TransactionDTO transaction, CreditResponse credit) {
    if (credit.getAmmount() == transaction.getAmmount()) {
      return Mono.just(transaction);
    }
    return Mono.error(new InvalidAmmount("Credit payment amount does not match credit amount"));
  }

  /**
   * Valida un cargo en una tarjeta de crédito.
   *
   * @param transaction la transacción a validar
   * @param creditCard  la respuesta del servicio que contiene la información de la tarjeta de crédito
   * @return la transacción validada si es válida, o un error si no lo es
   */
  public static Mono<TransactionDTO> validateCreditCardCharge(TransactionDTO transaction, CreditResponse creditCard) {
    if (creditCard.getAmmount() >= transaction.getAmmount()) {
      TransactionDTO mappedTransaction = GenericMapper.mapToAny(creditCard, TransactionDTO.class);
      return Mono.just(mappedTransaction);
    }
    return Mono.error(new InvalidAmmount("Credit card charge amount is too large"));
  }
}