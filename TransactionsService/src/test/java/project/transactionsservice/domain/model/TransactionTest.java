package project.transactionsservice.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionTest {

  private Transaction defaultTransaction;

  @BeforeEach
  void setUp() {
    defaultTransaction = createDefaultTransaction();
  }

  private Transaction createDefaultTransaction() {
    return new Transaction(
        "txn123",
        "prod123",
        TransactionType.DEPOSITO,
        100.0,
        new Date(),
        "client123",
        "detail"
    );
  }

  @Test
  void testDefaultConstructor() {
    Transaction transaction = new Transaction();
    assertThat(transaction).isNotNull();
    assertThat(transaction.getTransactionId()).isNull();
    assertThat(transaction.getProductNumber()).isEmpty();
    assertThat(transaction.getTransactionType()).isNull();
    assertThat(transaction.getAmmount()).isEqualTo(0.0);
    assertThat(transaction.getTransactionDate()).isNotNull();
    assertThat(transaction.getClientNumber()).isEmpty();
    assertThat(transaction.getTransactionDetail()).isEmpty();
  }

  @Test
  void testAllArgsConstructor() {
    assertThat(defaultTransaction).isNotNull();
    assertThat(defaultTransaction.getTransactionId()).isEqualTo("txn123");
    assertThat(defaultTransaction.getProductNumber()).isEqualTo("prod123");
    assertThat(defaultTransaction.getTransactionType()).isEqualTo(TransactionType.DEPOSITO);
    assertThat(defaultTransaction.getAmmount()).isEqualTo(100.0);
    assertThat(defaultTransaction.getTransactionDate()).isNotNull();
    assertThat(defaultTransaction.getClientNumber()).isEqualTo("client123");
    assertThat(defaultTransaction.getTransactionDetail()).isEqualTo("detail");
  }

  @Test
  void testSettersAndGetters() {
    String transactionId = "txn456";
    String productNumber = "prod456";
    TransactionType transactionType = TransactionType.RETIRO;
    double ammount = 200.0;
    Date transactionDate = new Date();
    String clientNumber = "client456";
    String transactionDetail = "detail456";

    defaultTransaction.setTransactionId(transactionId);
    defaultTransaction.setProductNumber(productNumber);
    defaultTransaction.setTransactionType(transactionType);
    defaultTransaction.setAmmount(ammount);
    defaultTransaction.setTransactionDate(transactionDate);
    defaultTransaction.setClientNumber(clientNumber);
    defaultTransaction.setTransactionDetail(transactionDetail);

    assertThat(defaultTransaction.getTransactionId()).isEqualTo(transactionId);
    assertThat(defaultTransaction.getProductNumber()).isEqualTo(productNumber);
    assertThat(defaultTransaction.getTransactionType()).isEqualTo(transactionType);
    assertThat(defaultTransaction.getAmmount()).isEqualTo(ammount);
    assertThat(defaultTransaction.getTransactionDate()).isEqualTo(transactionDate);
    assertThat(defaultTransaction.getClientNumber()).isEqualTo(clientNumber);
    assertThat(defaultTransaction.getTransactionDetail()).isEqualTo(transactionDetail);
  }
}
