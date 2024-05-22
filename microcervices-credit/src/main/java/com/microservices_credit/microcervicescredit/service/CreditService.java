package com.microservices_credit.microcervicescredit.service;

import com.microservices_credit.microcervicescredit.entity.credits;
import com.microservices_credit.microcervicescredit.repository.CreditRepository;
import org.springframework.stereotype.Service;

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

    public credits getCreditsById(Integer id){
        return creditRepository.findById(id).get();
    }
}
