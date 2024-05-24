package com.microservices_credit.microcervicescredit.apicalls.feignClient;

import com.microservices_credit.microcervicescredit.apicalls.ClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "clientService")
public interface ClientFeignClient { // Interfaz por cada servicio al que se quiera comunicar
  @GetMapping("/clients/{clientDocument}")
  ClientResponse getClient(@RequestParam("clientDocument") String clientDocument);
}
