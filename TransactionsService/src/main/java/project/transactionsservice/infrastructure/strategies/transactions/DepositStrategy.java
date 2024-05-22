package project.transactionsservice.domain.strategies.transactions;

import org.springframework.stereotype.Component;
import project.transactionsservice.domain.validations.TransactionDomainValidations;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.exceptions.throwable.NotFoundProduct;
import project.transactionsservice.infrastructure.factory.AccountsFactory;
import project.transactionsservice.infrastructure.mapper.GenericMapper;
import project.transactionsservice.infrastructure.servicecalls.AccountService;
import project.transactionsservice.infrastructure.servicecalls.request.AccountRequest;
import project.transactionsservice.infrastructure.servicecalls.responses.AccountResponse;
import reactor.core.publisher.Mono;

@Component
public class DepositStrategy implements TransactionStrategy {
  private final AccountService accountService;
  private final AccountsFactory accountsFactory;

  public DepositStrategy(AccountService accountService, AccountsFactory accountsFactory) {
    this.accountService = accountService;
    this.accountsFactory = accountsFactory;
  }

  public Mono<AccountResponse> getAccountDetails(String accountNumber) {
    return accountService.getAccount(accountNumber)
        .flatMap(response -> {
          if (!response.isSuccess()) {
            return Mono.error(new NotFoundProduct("Bank account not found"));
          }
          return Mono.justOrEmpty(response.getData())
              .map(data -> GenericMapper.mapToAny(data, AccountResponse.class));
        });
  }

  @Override
  public Mono<Object> execute(TransactionDTO transaction) {
    return getAccountDetails(transaction.getClientNumber())
        .flatMap(accountResponse -> {
          TransactionDomainValidations.validateDeposit(transaction)
              .flatMap(dto -> {
                double balance = accountResponse.getBalance();
                balance += transaction.getAmmount();
                AccountRequest request = new AccountRequest(balance, accountResponse.getTransactions() + 1);
                return accountService.updateAccount(dto.getProductNumber(), request)
                    .flatMap(Mono::just);
              });
          return Mono.just(accountResponse);
        });
  }
}