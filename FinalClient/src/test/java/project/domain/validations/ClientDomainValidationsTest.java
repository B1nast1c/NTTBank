package project.domain.validations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.infrastructure.dto.ClientDTO;
import project.infrastructure.exceptions.throwable.WrongClientType;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Test para la validación del tipo de cliente.
 */
class ClientDomainValidationsTest {
  ClientDomainValidations clientDomainValidations = new ClientDomainValidations();
  ClientDTO testClientDTO = new ClientDTO();

  @BeforeEach
  void setUp() {
    testClientDTO.setClientType("PERSONAL");
  }

  /**
   * Retorna PERSONAL por el tipo de cliente PERSONAL.
   */
  @Test
  void shouldReturnPersonal() {
    Mono<ClientDTO> result = clientDomainValidations.validateClientType(testClientDTO);
    StepVerifier.create(result)
        .expectNext(testClientDTO)
        .verifyComplete();
  }

  /**
   * Retorna EMPRESARIAL por el tipo de cliente EMPRESARIAL.
   */
  @Test
  void shouldReturnEmpresarial() {
    testClientDTO.setClientType("EMPRESARIAL");
    Mono<ClientDTO> result = clientDomainValidations.validateClientType(testClientDTO);
    StepVerifier.create(result)
        .expectNext(testClientDTO)
        .verifyComplete();
  }

  /**
   * Retorna una excepción pues el tipo de cliente no es válido.
   */
  @Test
  void shouldThrowExceptionWhenInvalidClientType() {
    testClientDTO.setClientType("INVALID_TYPE");
    Mono<ClientDTO> result = clientDomainValidations.validateClientType(testClientDTO);
    StepVerifier.create(result)
        .expectError(WrongClientType.class)
        .verify();
  }

  /**
   * Retorna una excepción pues el tipo de cliente es vacío.
   */
  @Test
  void shouldThrowExceptionWhenNullClientType() {
    testClientDTO.setClientType("");
    Mono<ClientDTO> result = clientDomainValidations.validateClientType(testClientDTO);
    StepVerifier.create(result)
        .expectError(NullPointerException.class);
  }
}

