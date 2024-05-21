package project.transactionsservice.infrastructure.servicecalls.webClient;

import project.transactionsservice.infrastructure.servicecalls.request.AccountRequest;
import project.transactionsservice.infrastructure.servicecalls.responses.GenericResponse;
import reactor.core.publisher.Mono;

public interface AccountWebInterface {
  Mono<GenericResponse> getAccount(String accountNumber);

  Mono<GenericResponse> updateAccount(String accountNumber, AccountRequest body);
}
