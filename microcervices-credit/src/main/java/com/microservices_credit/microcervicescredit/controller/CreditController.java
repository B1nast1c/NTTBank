package com.microservices_credit.microcervicescredit.controller;

import com.microservices_credit.microcervicescredit.entity.Credits;
import com.microservices_credit.microcervicescredit.entity.CustomResponse;
import com.microservices_credit.microcervicescredit.service.CreditService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Autor: Bryan Flores
 * Tarea: Microservicios de creditos
 * Fecha: 9/05/2024
 */

@RestController //appi rest
@RequestMapping("/credits") // Definimos una url especifica para el microservicio

// Va a ser una clase como un end point es decir una clase que va a exponer recursos
public class CreditController {
  private final CreditService creditService;  //nos dara un error

  public CreditController(CreditService creditService) {
    this.creditService = creditService;
  }

  //Metodo para poder guardar un credito nos permitira que el objeto json pase a mapearse en nuestra clase java
  @PostMapping(value = "/create")
  public CustomResponse<Object> createCredits(@RequestBody Credits Credits) {
    return creditService.createCredits(Credits);
  }

  @GetMapping(value = "/credit/{id}")     //nos permitira obtener un registro del servidor
  public CustomResponse<Object> getCreditById(@PathVariable String id) { //En la url viene ID y que el id deve de mapearlo a la variable id
    return creditService.getCreditsById(id);
  }

  @GetMapping(value = "/credit/client/{id}")     //nos permitira obtener un registro del servidor
  public CustomResponse<Object> getCreditByClientDocument(@PathVariable String id) { //En la url viene ID y que el id deve de mapearlo a la variable id
    return creditService.getCreditsByClientDocument(id);
  }

  //Creamos un nuevo metodo para traer todos de los creditos
  @GetMapping(value = "/all")     //traemos algo del servidor es decir todos los creditops
  public CustomResponse<List<Credits>> findAll() {
    return creditService.findAll();
  }

  //Modificamos un credito
  @PutMapping(value = "/update/{id}")
  public CustomResponse<Object> updateCredit(@PathVariable String id, @RequestBody Credits credit) {
    return creditService.updateCredit(id, credit);
  }

  //Eliminamos un credito
  @DeleteMapping(value = "/delete/{id}")
  public void deleteCredit(@PathVariable String id) {
    creditService.deleteCredit(id);
  }
}
