package project.Application.Service;

import project.infrastructure.dto.credit.CreditDTO;

import java.util.List;

public interface CreditService {
  CreditDTO createCredit(CreditDTO creditDTO);

  CreditDTO getCredit(String creditNumber);

  void updateCredit(String creditNumber, CreditDTO creditDTO);

  void deleteCredit(String creditNumber);

  List<CreditDTO> getAllCredits();

  List<CreditDTO> getCreditsByClientId(String clientId);
}
