package project.transactionsservice.infrastructure.helpers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import project.transactionsservice.infrastructure.exceptions.throwable.NotFoundProduct;
import project.transactionsservice.infrastructure.servicecalls.AccountService;
import project.transactionsservice.infrastructure.servicecalls.responses.AccountResponse;
import project.transactionsservice.infrastructure.servicecalls.responses.GenericResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class HelperFunctionsTest {
  @Mock
  private AccountService accountService;

  @InjectMocks
  private HelperFunctions helperFunctions;

  private GenericResponse successfulResponse;
  private GenericResponse unsuccessfulResponse;
  private AccountResponse accountResponse;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    accountResponse = new AccountResponse();
    accountResponse.setAccountType("AHORRO");
    accountResponse.setBalance(200.0);
    accountResponse.setTransactions(1);
    accountResponse.setAccountNumber("testProductNumber");

    successfulResponse = new GenericResponse(true, accountResponse);
    unsuccessfulResponse = new GenericResponse(false, null);
  }

  @Test
  void shouldGetTransactionDetails() {
    when(accountService.getAccount(anyString())).thenReturn(Mono.just(successfulResponse));

    Mono<AccountResponse> result = helperFunctions.getAccountDetails("testProductNumber");

    StepVerifier.create(result)
        .expectNextMatches(response -> response.getAccountNumber().equals("testProductNumber") &&
            response.getBalance() == 200.0)
        .verifyComplete();
  }

  @Test
  void shouldNotGetTransactionDetail() {
    when(accountService.getAccount(anyString())).thenReturn(Mono.just(unsuccessfulResponse));

    Mono<AccountResponse> result = helperFunctions.getAccountDetails("testProductNumber");

    StepVerifier.create(result)
        .expectError(NotFoundProduct.class)
        .verify();
  }
}

