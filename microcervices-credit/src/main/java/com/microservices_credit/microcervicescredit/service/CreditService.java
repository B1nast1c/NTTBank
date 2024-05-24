package com.microservices_credit.microcervicescredit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices_credit.microcervicescredit.apicalls.Client;
import com.microservices_credit.microcervicescredit.apicalls.ClientResponse;
import com.microservices_credit.microcervicescredit.entity.Credits;
import com.microservices_credit.microcervicescredit.entity.CustomResponse;
import com.microservices_credit.microcervicescredit.exceptions.throwable.NotFound;
import com.microservices_credit.microcervicescredit.repository.CreditRepository;
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

// Encapsula la lógica de negocio para manejar créditos en la aplicación,
// Ofreciendo métodos para crear, leer, actualizar y eliminar créditos
@Service
public class CreditService {
  private final CreditRepository creditRepository; //Injectamos la interfaz
  private final CreditAndCardHelper helpers;
  private final ObjectMapper mapper = new ObjectMapper();

  public CreditService(CreditRepository creditRepository, CreditAndCardHelper helpers) {
    this.creditRepository = creditRepository;
    this.helpers = helpers;
  }

  private Optional<Credits> findCreditById(String id) { // Id -> NUMERO DE CREDITO
    return Optional.ofNullable(creditRepository.findByCreditNumber(id));
  }

  //Guardamos el objeto créditos por la base de datos y nos retorna el objeto ya creado
  public CustomResponse<Object> createCredits(Credits credit) {
    try {
      ClientResponse foundClient = helpers.validateClient(credit.getClientDocument());
      Client extractedClient = mapper.convertValue(foundClient.getData(), Client.class);
      List<Credits> foundClients = creditRepository.findAllByClientDocument(extractedClient.getDocumentNumber());
      CreditValidations.validateClient(foundClient);
      CreditValidations.validateCreditsAmmount(credit, foundClients, extractedClient.getClientType());
      credit.setCreditNumber(credit.generateCreditNumber()); // Asignación del credito aleatorio
      credit.setCreditType(extractedClient.getClientType());
      Credits savedObject = creditRepository.save(credit);
      return new CustomResponse<>(true, savedObject);
    } catch (Exception e) {
      return new CustomResponse<>(false, e.getMessage());
    }
  }

  public CustomResponse<Object> getCreditsById(String id) {
    try {
      Optional<Credits> foundObject = findCreditById(id);
      if (foundObject.isEmpty()) {
        throw new NotFound("Credit not found");
      }
      return new CustomResponse<>(true, foundObject);
    } catch (Exception e) {
      return new CustomResponse<>(false, e.getMessage());
    }
  }

  //Crear metodo para obtener todos los creditos
  public CustomResponse<List<Credits>> findAll() {
    return new CustomResponse<>(true, creditRepository.findAll());
  }

  //Modificar Creditos
  public CustomResponse<Object> updateCredit(String id, Credits credit) {
    try {
      Optional<Credits> creditOptional = findCreditById(id);
      if (creditOptional.isEmpty()) {
        throw new NotFound("Credit not found");
      }
      Credits gottenCredit = creditOptional.get();
      gottenCredit.setPaid(credit.isPaid());
      gottenCredit.setAmmount(credit.getAmmount());
      creditRepository.save(gottenCredit);
      return new CustomResponse<>(true, "Credit updated successfully");
    } catch (Exception e) {
      return new CustomResponse<>(false, e.getMessage());
    }
  }

  //Eliminar Credito Por ID
  public CustomResponse<Object> deleteCredit(String id) {
    try {
      creditRepository.deleteById(id);
      return new CustomResponse<>(true, "Credit deleted successfully");
    } catch (Exception e) {
      return new CustomResponse<>(false, e.getMessage());
    }
  }

  public CustomResponse<Object> getCreditsByClientDocument(String clientDocument) {
    List<Credits> foundObject = creditRepository.findAllByClientDocument(clientDocument);
    if (foundObject.isEmpty()) {
      throw new NotFound("Credit not found");
    }
    return new CustomResponse<>(true, foundObject);
  }
}
