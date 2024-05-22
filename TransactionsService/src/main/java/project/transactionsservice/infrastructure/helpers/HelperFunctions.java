package project.transactionsservice.infrastructure.helpers;

import org.springframework.stereotype.Component;
import project.transactionsservice.infrastructure.exceptions.throwable.NotFoundProduct;
import project.transactionsservice.infrastructure.mapper.GenericMapper;
import project.transactionsservice.infrastructure.servicecalls.AccountService;
import project.transactionsservice.infrastructure.servicecalls.responses.AccountResponse;
import reactor.core.publisher.Mono;

@Component
public class HelperFunctions {
  private final AccountService accountService;

  public HelperFunctions(AccountService accountService) {
    this.accountService = accountService;
  }

  public Mono<AccountResponse> getAccountDetails(String accountNumber) {
    return accountService.getAccount(accountNumber)
        .flatMap(response -> {
          if (!response.isSuccess()) {
            return Mono.error(new NotFoundProduct("Bank account not found"));
          }
          AccountResponse accountResponse = GenericMapper.mapToAny(response.getData(), AccountResponse.class);
          return Mono.just(accountResponse);
        });
  }
}
