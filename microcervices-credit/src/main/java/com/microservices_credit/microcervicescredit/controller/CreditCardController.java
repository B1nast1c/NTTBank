package com.microservices_credit.microcervicescredit.controller;

import com.microservices_credit.microcervicescredit.entity.CreditCard;
import com.microservices_credit.microcervicescredit.entity.CustomResponse;
import com.microservices_credit.microcervicescredit.service.CreditCardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Autor: Bryan Flores
 * Tarea: Microservicios de creditos
 * Fecha: 9/05/2024
 */

@RestController //api rest
@RequestMapping("/cards") // Definimos una url especifica para el microservicio
public class CreditCardController {
  private final CreditCardService creditService; //nos dara un error

  public CreditCardController(CreditCardService creditService) {
    this.creditService = creditService;
  }

  //Metodo para poder guardar un credito nos permitira que el objeto json pase a mapearse en nuestra clase java
  @PostMapping(value = "/create")
  public CustomResponse<Object> createCredits(@RequestBody CreditCard creditCard) {
    return creditService.createCreditCards(creditCard);
  }

  @GetMapping(value = "/card/{id}")     //nos permitira obtener un registro del servidor
  public CustomResponse<Object> getCreditById(@PathVariable String id) { //En la url viene ID y que el id deve de mapearlo a la variable id
    return creditService.getCardsById(id);
  }

  @GetMapping(value = "/card/client/{id}")     //nos permitira obtener un registro del servidor
  public CustomResponse<Object> getCreditByClientDocument(@PathVariable String id) { //En la url viene ID y que el id deve de mapearlo a la variable id
    return creditService.getCardByClientDocument(id);
  }

  //Creamos un nuevo metodo para traer todos de los creditos
  @GetMapping(value = "/all")     //traemos algo del servidor es decir todos los creditops
  public CustomResponse<List<CreditCard>> findAll() {
    return creditService.findAll();
  }

  //Modificamos un credito
  @PutMapping(value = "/update/{id}")
  public CustomResponse<Object> updateCredit(@PathVariable String id, @RequestBody CreditCard creditCard) {
    return creditService.updateCredit(id, creditCard);
  }

  //Eliminamos un credito
  @DeleteMapping(value = "/delete/{id}")
  public void deleteCredit(@PathVariable String id) {
    creditService.deleteCreditCard(id);
  }
}
