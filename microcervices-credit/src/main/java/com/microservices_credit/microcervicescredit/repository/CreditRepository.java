package com.microservices_credit.microcervicescredit.repository;
import com.microservices_credit.microcervicescredit.entity.credits;
import org.springframework.data.mongodb.repository.MongoRepository;



/**
 * @Autor: Bryan Flores
 * Tarea: Microservicios de creditos
 * Fecha: 9/05/2024
 */




public interface CreditRepository extends MongoRepository<credits, String> {
}
