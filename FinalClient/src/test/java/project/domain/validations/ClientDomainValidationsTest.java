package project.domain.validations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
class ClientDomainValidationsTest {
    project.domain.validations.ClientDomainValidations clientDomainValidations = new project.domain.validations.ClientDomainValidations();

    @Test
    void testValidateClientType(){
        reactor.core.publisher.Mono<project.infrastructure.dto.ClientDTO> result = clientDomainValidations.validateClientType(new project.infrastructure.dto.ClientDTO("customId", "clientType", "clientName", "clientAddress", "clientEmail", "clientPhone", "documentNumber", Boolean.TRUE, "createdAt"));
        Assertions.assertEquals(null, result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme