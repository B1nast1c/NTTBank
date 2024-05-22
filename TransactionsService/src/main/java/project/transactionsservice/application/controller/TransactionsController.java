package project.transactionsservice.application.controller;

import org.springframework.web.bind.annotation.*;
import project.transactionsservice.application.service.TransactionsService;
import project.transactionsservice.infrastructure.dto.TransactionDTO;
import project.transactionsservice.infrastructure.exceptions.CustomResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionsController {
  private final TransactionsService transactionsService;

  public TransactionsController(TransactionsService transactionsService) {
    this.transactionsService = transactionsService;
  }

  @GetMapping(value = "/all")
  public Mono<CustomResponse<List<TransactionDTO>>> getAllTransactions() {
    return transactionsService.getAllTransactions();
  }

  @PostMapping(value = "/create")
  public Mono<CustomResponse<Object>> createTransactions(@RequestBody TransactionDTO transaction) {
    return transactionsService.createTransaction(transaction);
  }

  @GetMapping(value = "/transaction/{transactionId}")
  public Mono<CustomResponse<Object>> getTransactionById(@PathVariable("transactionId") String transactionId) {
    return transactionsService.getTransaction(transactionId);
  }

  @GetMapping(value = "/bankProduct/{productNumber}")
  public Mono<CustomResponse<Object>> getTransactionsByProductNumber(@PathVariable("productNumber") String productNumber) {
    return transactionsService.getAllTransactionsByProduct(productNumber);
  }
}