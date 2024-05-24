package project.transactionsservice.infrastructure.servicecalls.webClient;

import project.transactionsservice.infrastructure.servicecalls.request.CreditRequest;
import project.transactionsservice.infrastructure.servicecalls.responses.GenericResponse;
import reactor.core.publisher.Mono;

public interface CreditWebInterface {
  Mono<GenericResponse> getCredit(String creditId);

  Mono<GenericResponse> updateCredit(String creditId, CreditRequest request);

  Mono<GenericResponse> getCreditCard(String creditId);

  Mono<GenericResponse> updateCreditCard(String creditId, CreditRequest request);
}
