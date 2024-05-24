package project.transactionsservice.application.controller;

import org.springframework.web.bind.annotation.*;
import project.transactionsservice.application.service.TransactionsService;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.exceptions.CustomResponse;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Controlador para manejar las transacciones.
 */
@RestController
@RequestMapping("/transactions")
public class TransactionsController {
  private final TransactionsService transactionsService;

  /**
   * Crea una nueva instancia del controlador de transacciones.
   *
   * @param transactionsService el servicio de transacciones
   */
  public TransactionsController(TransactionsService transactionsService) {
    this.transactionsService = transactionsService;
  }

  /**
   * Obtiene todas las transacciones.
   *
   * @return una respuesta con todas las transacciones
   */
  @GetMapping(value = "/all")
  public Mono<CustomResponse<List<TransactionDTO>>> getAllTransactions() {
    return transactionsService.getAllTransactions();
  }

  /**
   * Crea una nueva transacción.
   *
   * @param transaction la transacción a crear
   * @return una respuesta indicando el resultado de la creación
   */
  @PostMapping(value = "/create")
  public Mono<CustomResponse<Object>> createTransactions(@RequestBody TransactionDTO transaction) {
    return transactionsService.createTransaction(transaction);
  }

  /**
   * Obtiene una transacción por su ID.
   *
   * @param transactionId el ID de la transacción
   * @return una respuesta con la transacción encontrada
   */
  @GetMapping(value = "/transaction/{transactionId}")
  public Mono<CustomResponse<Object>> getTransactionById(@PathVariable("transactionId") String transactionId) {
    return transactionsService.getTransaction(transactionId);
  }

  /**
   * Obtiene transacciones por el número del producto bancario.
   *
   * @param productNumber el número del producto bancario
   * @return una respuesta con las transacciones relacionadas al producto
   */
  @GetMapping(value = "/bankProduct/{productNumber}")
  public Mono<CustomResponse<Object>> getTransactionsByProductNumber(@PathVariable("productNumber") String productNumber) {
    return transactionsService.getAllTransactionsByProduct(productNumber);
  }
}