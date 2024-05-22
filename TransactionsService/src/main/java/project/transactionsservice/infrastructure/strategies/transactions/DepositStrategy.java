package project.transactionsservice.infrastructure.strategies.transactions;

import org.springframework.stereotype.Component;
import project.transactionsservice.domain.validations.TransactionDomainValidations;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.exceptions.throwable.InvalidTransaction;
import project.transactionsservice.infrastructure.factory.AccountsFactory;
import project.transactionsservice.infrastructure.helpers.HelperFunctions;
import project.transactionsservice.infrastructure.mapper.GenericMapper;
import project.transactionsservice.infrastructure.servicecalls.AccountService;
import project.transactionsservice.infrastructure.servicecalls.request.AccountRequest;
import project.transactionsservice.infrastructure.servicecalls.responses.AccountResponse;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

@Component("depositStrategy")
public class DepositStrategy implements TransactionStrategy {
  private final AccountsFactory accountsFactory;
  private final AccountService accountService;
  private final HelperFunctions helpers;

  public DepositStrategy(AccountsFactory accountsFactory,
                         AccountService accountService,
                         HelperFunctions helpers) {
    this.accountsFactory = accountsFactory;
    this.accountService = accountService;
    this.helpers = helpers;
  }

  @Override
  public Mono<Object> execute(TransactionDTO transaction) {
    return helpers.getAccountDetails(transaction.getProductNumber())
        .flatMap(accountResponse -> {
          BiFunction<TransactionDTO, AccountResponse, Mono<TransactionDTO>> strategy;
          strategy = accountsFactory.getStrategy(accountResponse.getAccountType());
          return strategy.apply(transaction, accountResponse).flatMap(applied ->
              TransactionDomainValidations.validateDeposit(transaction)
                  .flatMap(dto -> {
                    double balance = accountResponse.getBalance();
                    balance += transaction.getAmmount();
                    AccountRequest request = new AccountRequest(balance, accountResponse.getTransactions() + 1);
                    return accountService
                        .updateAccount(dto.getProductNumber(), request)
                        .flatMap(updated -> {
                          if (updated.isSuccess()) {
                            Object mapped = GenericMapper.mapToAny(transaction, Object.class);
                            return Mono.just(mapped);
                          }
                          return Mono.error(new InvalidTransaction("Transactions limit reached"));
                        });
                  }));
        });
  }
}