package com.microservices_credit.microcervicescredit.repository;

import com.microservices_credit.microcervicescredit.entity.Credits;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Autor: Bryan Flores
 * Tarea: Microservicios de creditos
 * Fecha: 9/05/2024
 */

//Extendemos el método a MongoRepository para proporcionar automáticamente una implementación
// de los métodos CRUD básicos (Create, Read, Update, Delete)

@Repository
public interface CreditRepository extends MongoRepository<Credits, String> {
  List<Credits> findAllByClientDocument(String document);

  Credits findByCreditNumber(String number);
}
