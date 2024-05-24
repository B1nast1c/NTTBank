package com.microservices_credit.microcervicescredit.repository;
import com.microservices_credit.microcervicescredit.entity.credits;
import org.springframework.data.mongodb.repository.MongoRepository;



/**
 * @Autor: Bryan Flores
 * Tarea: Microservicios de creditos
 * Fecha: 9/05/2024
 */



//estendemos el metodo a MongoRepository para proporcionar automáticamente una implementación
// de los métodos CRUD básicos (Create, Read, Update, Delete)
public interface CreditRepository extends MongoRepository<credits, String> {
}
