package com.microservices_credit.microcervicescredit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices_credit.microcervicescredit.apicalls.ClientResponse;
import com.microservices_credit.microcervicescredit.entity.CreditCard;
import com.microservices_credit.microcervicescredit.entity.CustomResponse;
import com.microservices_credit.microcervicescredit.exceptions.throwable.NotFound;
import com.microservices_credit.microcervicescredit.repository.CreditCardRepository;
import com.microservices_credit.microcervicescredit.service.helpers.CreditAndCardHelper;
import com.microservices_credit.microcervicescredit.validations.CreditValidations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Autor: Bryan Flores
 * Tarea: Microservicios de creditos
 * Fecha: 9/05/2024
 */

// Servicio para tarjetas de credito
@Service
public class CreditCardService {
  private final CreditCardRepository creditCardRepository; //Injectamos la interfaz
  private final CreditAndCardHelper helpers;
  private final ObjectMapper mapper = new ObjectMapper();

  public CreditCardService(CreditCardRepository creditCardRepository, CreditAndCardHelper helpers) {
    this.creditCardRepository = creditCardRepository;
    this.helpers = helpers;
  }

  private Optional<CreditCard> findCreditCardById(String id) { // Id -> NUMERO DE CREDITO
    return Optional.ofNullable(creditCardRepository.findByCardNumber(id));
  }

  public CustomResponse<Object> createCreditCards(CreditCard creditCard) {
    try {
      ClientResponse foundClient = helpers.validateClient(creditCard.getClientDocument());
      CreditValidations.validateClient(foundClient);
      CreditValidations.validateCreditCard(creditCardRepository.existsByClientDocument(creditCard.getClientDocument()));
      creditCard.setCardNumber(creditCard.generateCardNumber());
      validateLimit(creditCard.getLimit(), creditCard.getAmmount());
      CreditCard savedObject = creditCardRepository.save(creditCard);
      return new CustomResponse<>(true, savedObject);
    } catch (Exception e) {
      return new CustomResponse<>(false, e.getMessage());
    }
  }

  public CustomResponse<Object> getCardsById(String id) {
    try {
      Optional<CreditCard> foundObject = findCreditCardById(id);
      if (foundObject.isEmpty()) {
        throw new NotFound("Credit card not found");
      }
      return new CustomResponse<>(true, foundObject);
    } catch (Exception e) {
      return new CustomResponse<>(false, e.getMessage());
    }
  }

  //Crear metodo para obtener todas las tarjetas de credito
  public CustomResponse<List<CreditCard>> findAll() {
    return new CustomResponse<>(true, creditCardRepository.findAll());
  }

  private void validateLimit(double limit, double ammount) {
    if (ammount > limit) {
      throw new NotFound("Credit card limit exceeded");
    }
  }

  //Modificar tarjeta de credito
  public CustomResponse<Object> updateCredit(String id, CreditCard creditCard) {
    try {
      Optional<CreditCard> creditOptional = findCreditCardById(id);
      if (creditOptional.isEmpty()) {
        throw new NotFound("Credit card not found");
      }
      CreditCard gottenCard = creditOptional.get();
      validateLimit(creditOptional.get().getLimit(), creditCard.getAmmount());
      gottenCard.setLimit(creditOptional.get().getLimit());
      gottenCard.setAmmount(creditCard.getAmmount());
      creditCardRepository.save(gottenCard);
      return new CustomResponse<>(true, "Credit card updated successfully");
    } catch (Exception e) {
      return new CustomResponse<>(false, e.getMessage());
    }
  }

  //Eliminar Credito Por ID
  public CustomResponse<Object> deleteCreditCard(String id) {
    try {
      creditCardRepository.deleteById(id);
      return new CustomResponse<>(true, "Credit card deleted successfully");
    } catch (Exception e) {
      return new CustomResponse<>(false, e.getMessage());
    }
  }

  public CustomResponse<Object> getCardByClientDocument(String clientDocument) {
    Optional<CreditCard> foundObject = Optional
        .ofNullable(creditCardRepository.findByClientDocument(clientDocument));
    if (foundObject.isEmpty()) {
      throw new NotFound("Credit card not found or does not exist");
    }
    return new CustomResponse<>(true, foundObject);
  }
}
