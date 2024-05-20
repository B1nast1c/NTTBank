package project.Application.Service;

import project.infrastructure.dto.credit.CreditCardDTO;

import java.util.List;

public interface CreditCardService {
  CreditCardDTO createCreditCard(CreditCardDTO creditCardDTO);

  CreditCardDTO getCreditCard(String cardNumber);

  CreditCardDTO updateCreditCard(String cardNumber, CreditCardDTO creditCardDTO);

  CreditCardDTO deleteCreditCard(String cardNumber);

  List<CreditCardDTO> getCreditCards();
}
