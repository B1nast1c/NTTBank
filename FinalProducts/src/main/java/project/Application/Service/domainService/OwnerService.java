package project.Application.Service.domainService;

import project.infrastructure.dto.account.LegalSignerDTO;
import project.infrastructure.dto.account.TitularDTO;

public interface OwnerService {
  void updateTitularData(TitularDTO titularDTO);

  void updateLegalSignerData(LegalSignerDTO legalSignerDTO);
}
