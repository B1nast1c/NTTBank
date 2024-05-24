package com.microservices_credit.microcervicescredit.repository;

import com.microservices_credit.microcervicescredit.entity.CreditCard;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardRepository extends MongoRepository<CreditCard, String> {
  CreditCard findByCardNumber(String cardNumber);

  CreditCard findByClientDocument(String clientDocument);

  Boolean existsByClientDocument(String clientDocument);
}
