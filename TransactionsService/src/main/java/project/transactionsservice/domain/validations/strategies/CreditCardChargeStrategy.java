package project.transactionsservice.domain.validations.strategies;

import project.transactionsservice.domain.validations.TransactionDomainValidations;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.mapper.GenericMapper;
import project.transactionsservice.infrastructure.serviceCalls.responses.CreditResponse;
import project.transactionsservice.infrastructure.serviceCalls.responses.GenericResponse;
import reactor.core.publisher.Mono;

public class CreditCardChargeStrategy extends TransactionDomainValidations {
  public Mono<TransactionDTO> saveCreditCardCharge(TransactionDTO transactionDTO) {
    return null;
  }

  @Override
  protected Mono<TransactionDTO> validateTransaction(TransactionDTO transaction, GenericResponse serviceResponse) {
    CreditResponse creditResponse = GenericMapper.mapToAny(serviceResponse.getData(), CreditResponse.class);

    return TransactionDomainValidations.validateProduct(transaction, serviceResponse)
        .flatMap(res -> TransactionDomainValidations.validateCreditCardCharge(transaction, creditResponse));
  }
}
