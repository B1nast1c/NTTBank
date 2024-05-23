package com.microservices_credit.microcervicescredit.controller;

import com.microservices_credit.microcervicescredit.entity.credits;
import com.microservices_credit.microcervicescredit.service.CreditService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //appi rest
@RequestMapping("/api/credits") // Definimos una url especifica para el microservico

// Va a ser una clase como un end point es decir una clase que va a exponer recursos
public class CreditController {
    private final CreditService creditService;  //nos dara un error
    //para ello la injectamos por un constructor


    public CreditController(CreditService creditService) {
        this.creditService = creditService;
    }

    @PostMapping        //Metodo para poder guardar un creditp
    public credits createCredits(@RequestBody credits Credits){ //nos permitira que el forto json pase a nuestra clase java
        return creditService.CreateCredits(Credits);
    }

    @GetMapping("/{id}")     //nos permitira obtener un registro del servidor
    public credits getCreditById(@PathVariable Integer id){ //En la url viene ID y que el id deve de mapearlo a la variable id
        return creditService.getCreditsById(id);
    }

    //Creamos un nuevo metodo para traer todos los departamentos
    @GetMapping     //traemos algo del servidor es decir todos los creditops
    public List<credits>findAll(){
        return creditService.finAll();
    }
}
