package project.transactionsservice.infrastructure.strategies;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
//import static org.mockito.Mockito.*;
class AccountStrategiesTest {
    @Mock
    project.transactionsservice.infrastructure.adapters.mongoRepos.TransactionsRepo transactionsRepo;
    @InjectMocks
    project.transactionsservice.infrastructure.strategies.AccountStrategies accountStrategies;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSavingsStrategy(){
        reactor.core.publisher.Mono<project.transactionsservice.infrastructure.dto.TransactionDTO> result = accountStrategies.savingsStrategy(new project.transactionsservice.infrastructure.dto.TransactionDTO("transactionId", "productNumber", "transactionType", "transactionDate", "clientNumber", "transactionDetail", 0d), new project.transactionsservice.infrastructure.servicecalls.responses.AccountResponse("accountNumber", "accountType", "clientDocument", 0d, 0, 0d, java.util.List.of("accountTitulars"), java.util.List.of("legalSigners"), new java.util.GregorianCalendar(2024, java.util.Calendar.MAY, 22, 20, 19).getTime(), 0));
        Assertions.assertEquals(null, result);
    }

    @Test
    void testFxdStrategy(){
        when(transactionsRepo.findAllByProductNumber(anyString())).thenReturn(null);

        reactor.core.publisher.Mono<project.transactionsservice.infrastructure.dto.TransactionDTO> result = accountStrategies.fxdStrategy(new project.transactionsservice.infrastructure.dto.TransactionDTO("transactionId", "productNumber", "transactionType", "transactionDate", "clientNumber", "transactionDetail", 0d), new project.transactionsservice.infrastructure.servicecalls.responses.AccountResponse("accountNumber", "accountType", "clientDocument", 0d, 0, 0d, java.util.List.of("accountTitulars"), java.util.List.of("legalSigners"), new java.util.GregorianCalendar(2024, java.util.Calendar.MAY, 22, 20, 19).getTime(), 0));
        Assertions.assertEquals(null, result);
    }

    @Test
    void testCurrAccStrategy(){
        reactor.core.publisher.Mono<project.transactionsservice.infrastructure.dto.TransactionDTO> result = accountStrategies.currAccStrategy(new project.transactionsservice.infrastructure.dto.TransactionDTO("transactionId", "productNumber", "transactionType", "transactionDate", "clientNumber", "transactionDetail", 0d), new project.transactionsservice.infrastructure.servicecalls.responses.AccountResponse("accountNumber", "accountType", "clientDocument", 0d, 0, 0d, java.util.List.of("accountTitulars"), java.util.List.of("legalSigners"), new java.util.GregorianCalendar(2024, java.util.Calendar.MAY, 22, 20, 19).getTime(), 0));
        Assertions.assertEquals(null, result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme