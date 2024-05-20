package com.nttdata.project.bank.account.infrastructure.mapper;

import com.nttdata.project.bank.account.domain.model.BankAccount;
import com.nttdata.project.bank.account.infrastructure.dto.BankAccountDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class GenericMapper {
  private static final ModelMapper modelMapper = new ModelMapper();

  private GenericMapper() {
  }

  public static BankAccount mapToEntity(final Object bankAccountDTO) {
    return modelMapper.map(bankAccountDTO, BankAccount.class);
  }

  public static BankAccountDTO mapToDTO(final Object bankAccount) {
    return modelMapper.map(bankAccount, BankAccountDTO.class);
  }
}
