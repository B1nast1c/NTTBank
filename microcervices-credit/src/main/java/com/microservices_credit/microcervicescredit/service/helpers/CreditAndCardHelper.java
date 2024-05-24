package com.microservices_credit.microcervicescredit.service.helpers;

import com.microservices_credit.microcervicescredit.apicalls.ClientResponse;
import com.microservices_credit.microcervicescredit.apicalls.feignClient.ClientFeignClient;
import com.microservices_credit.microcervicescredit.exceptions.throwable.NotFound;
import org.springframework.stereotype.Component;

@Component
public class CreditAndCardHelper {
  private final ClientFeignClient clientService;

  public CreditAndCardHelper(ClientFeignClient clientService) {
    this.clientService = clientService;
  }

  public ClientResponse validateClient(String clientDocument) {
    ClientResponse clientResponse = clientService.getClient(clientDocument);
    if (clientResponse.isSuccess()) {
      return clientResponse;
    } else {
      throw new NotFound("Client not found in Clients Microservice");
    }
  }
}