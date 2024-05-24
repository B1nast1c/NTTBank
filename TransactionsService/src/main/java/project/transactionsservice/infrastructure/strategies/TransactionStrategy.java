package project.transactionsservice.infrastructure.strategies;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Component
public class TransactionStrategy {
  private final AccountsFactory accountsFactory;
  private final AccountService accountService;
  private final HelperFunctions helpers;
  BiFunction<TransactionDTO, AccountResponse, Mono<TransactionDTO>> strategy;

  public TransactionStrategy(AccountsFactory accountsFactory,
                             AccountService accountService,
                             HelperFunctions helpers) {
    this.accountsFactory = accountsFactory;
    this.accountService = accountService;
    this.helpers = helpers;
  }

  private Mono<Object> saveTransaction(TransactionDTO transaction, double ammount) {
    return helpers.getAccountDetails(transaction.getProductNumber())
        .flatMap(accountResponse -> {
          strategy = accountsFactory.getStrategy(accountResponse.getAccountType());
          return strategy.apply(transaction, accountResponse).flatMap(applied ->
              TransactionDomainValidations.validateDeposit(transaction)
                  .flatMap(dto -> {
                    double balance = accountResponse.getBalance();
                    balance += ammount;
                    AccountRequest request = new AccountRequest(balance, accountResponse.getTransactions() + 1);
                    return accountService
                        .updateAccount(dto.getProductNumber(), request)
                        .flatMap(updated -> {
                          if (updated.isSuccess()) {
                            Object mapped = GenericMapper.mapToAny(transaction, Object.class);
                            return Mono.just(mapped);
                          }
                          log.warn("Failed to update account request to the service -> accountService");
                          return Mono.error(new InvalidTransaction(updated.getData().toString()));
                        });
                  }));
        });
  }

  public Mono<Object> depositStrategy(TransactionDTO transaction) {
    return saveTransaction(transaction, transaction.getAmmount());
  }

  public Mono<Object> withdrawalStrategy(TransactionDTO transaction) {
    return saveTransaction(transaction, transaction.getAmmount() * -1);
  }
}