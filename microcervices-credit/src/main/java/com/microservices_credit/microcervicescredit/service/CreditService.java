package com.microservices_credit.microcervicescredit.service;

import com.microservices_credit.microcervicescredit.entity.credits;
import com.microservices_credit.microcervicescredit.repository.CreditRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditService {
    private final CreditRepository creditRepository;   //Injectamos la interfaz

    //Injectamos mediante el constructor la interfaz
    public CreditService(CreditRepository creditRepository) {
        this.creditRepository = creditRepository;
    }


    //Guardamos el objeto creditos por data base y nos retorna el obeto ya creado
    public credits CreateCredits(credits Credits){
        return creditRepository.save(Credits);
    }

    public credits getCreditsById(String id){
        return creditRepository.findById(id).get();
    }

    //Crear metodo para obtener todos los creditos
    public List<credits> finAll(){
        return creditRepository.findAll();
    }
    //modificar Creditos
    public credits updateCredit(String id, credits credit) {
        credit.setId(id);
        return creditRepository.save(credit);
    }

    //Eliminar Creditos
    public void deleteCredit(String id) {
        creditRepository.deleteById(id);
    }
}
