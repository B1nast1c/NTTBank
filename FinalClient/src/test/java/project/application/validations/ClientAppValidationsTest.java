package project.application.validations;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
//import static org.mockito.Mockito.*;

public class ClientAppValidationsTest {
    @Mock
    project.infrastructure.adapters.mongoRepos.ClientRepository clientRepository;
    @InjectMocks
    project.application.validations.ClientAppValidations clientAppValidations;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidateDocumentNumber() throws Exception {
        when(clientRepository.existsByDocumentNumber(anyString())).thenReturn(null);

        reactor.core.publisher.Mono<project.infrastructure.dto.ClientDTO> result = clientAppValidations.validateDocumentNumber(new project.infrastructure.dto.ClientDTO("customId", "clientType", "clientName", "clientAddress", "clientEmail", "clientPhone", "documentNumber", Boolean.TRUE, "createdAt"));
        Assert.assertEquals(null, result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme