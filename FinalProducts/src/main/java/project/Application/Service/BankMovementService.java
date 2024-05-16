package project.Application.Service;

import project.infrastructure.dto.BankMovementDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BankMovementService {
  Mono<BankMovementDTO> addMovement(Mono<BankMovementDTO> bankMovementDTO);

  Mono<BankMovementDTO> getMovement(String bankMovementNumber);

  Mono<Void> updateMovement(Mono<BankMovementDTO> bankMovementDTO);

  Mono<Void> deleteMovement(Mono<BankMovementDTO> bankMovementDTO);

  Flux<BankMovementDTO> findAllMovements();
}
