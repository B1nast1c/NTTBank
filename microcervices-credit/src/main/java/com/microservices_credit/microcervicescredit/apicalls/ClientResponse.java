package com.microservices_credit.microcervicescredit.apicalls;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ClientResponse {
  boolean success;
  Object data;
}
