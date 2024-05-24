package com.microservices_credit.microcervicescredit.repository;
import com.microservices_credit.microcervicescredit.entity.credits;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface CreditRepository extends MongoRepository<credits, String> {
}
