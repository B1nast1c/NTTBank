package project.transactionsservice.domain.strategies;

import org.springframework.stereotype.Component;
import project.transactionsservice.application.validations.TransactionsAppValidations;
import project.transactionsservice.domain.validations.TransactionDomainValidations;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.factory.AccountsFactory;
import project.transactionsservice.infrastructure.mapper.GenericMapper;
import project.transactionsservice.infrastructure.servicecalls.AccountService;
import project.transactionsservice.infrastructure.servicecalls.request.AccountRequest;
import project.transactionsservice.infrastructure.servicecalls.responses.AccountResponse;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

@Component
public class TransactionsStrategies {
  private final AccountService accountService;
  private final AccountsFactory accountsFactory;

  public TransactionsStrategies(AccountService accountService, AccountsFactory accountsFactory) {
    this.accountService = accountService;
    this.accountsFactory = accountsFactory;
  }

  public Mono<Object> depositAndWithdrawalStrategy(TransactionDTO transaction) {
    // Obtencion de datos de la cuenta bancaria si es que existe

    return accountService.getAccount(transaction.getProductNumber())
        .flatMap(response -> {
              AccountResponse accountResponse = GenericMapper.mapToAny(response.getData(), AccountResponse.class);
              String accountType = accountResponse.getAccountType();

              return TransactionsAppValidations.validateProduct(transaction, response)
                  .flatMap(validated -> {
                    BiFunction<TransactionDTO, AccountResponse, Mono<TransactionDTO>> strategy = accountsFactory.getStrategy(accountType);
                    strategy.apply(transaction, accountResponse); // Validación correspondiente a los clientes (AUTORIZADOS)

                    // Validaciones correspondientes a los tipos de transacciones
                    String transactionType = transaction.getTransactionType();
                    switch (transactionType) {
                      case "DEPOSITO":
                        return TransactionDomainValidations.validateDeposit(transaction)
                            .flatMap(dto -> {
                              double balance =  accountResponse.getBalance();
                              balance += transaction.getAmmount();
                              AccountRequest request = new AccountRequest(balance, accountResponse.getTransactions() + 1);
                              return accountService.updateAccount(dto.getProductNumber(), request)
                                  .flatMap(updated -> Mono.just(updated));
                            });
                      case "RETIRO":
                        return TransactionDomainValidations.validateWithdrawal(transaction, accountResponse)
                            .map(dto -> {
                              System.out.println("ENTIDAD RETIRO -> " + dto);
                              return dto;
                            });
                    }
                    return null;
                  });
            }
        );
  }

  public Mono<Object> creditPaymentStrategy(TransactionDTO transaction) {
    return null;
  }

  public Mono<Object> creditCardChargeStrategy(TransactionDTO transaction) {
    return null;
  }
}
