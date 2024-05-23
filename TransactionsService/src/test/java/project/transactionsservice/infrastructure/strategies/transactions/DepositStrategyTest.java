package project.transactionsservice.infrastructure.strategies.transactions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
//import static org.mockito.Mockito.*;
class DepositStrategyTest {
    @Mock
    project.transactionsservice.infrastructure.factory.AccountsFactory accountsFactory;
    @Mock
    project.transactionsservice.infrastructure.servicecalls.AccountService accountService;
    @Mock
    project.transactionsservice.infrastructure.helpers.HelperFunctions helpers;
    @InjectMocks
    project.transactionsservice.infrastructure.strategies.transactions.DepositStrategy depositStrategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecute(){
        when(accountsFactory.getStrategy(anyString())).thenReturn(null);
        when(accountService.updateAccount(anyString(), any(project.transactionsservice.infrastructure.servicecalls.request.AccountRequest.class))).thenReturn(null);
        when(helpers.getAccountDetails(anyString())).thenReturn(null);

        reactor.core.publisher.Mono<java.lang.Object> result = depositStrategy.execute(new project.transactionsservice.infrastructure.dto.TransactionDTO("transactionId", "productNumber", "transactionType", "transactionDate", "clientNumber", "transactionDetail", 0d));
        Assertions.assertEquals(null, result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme