package com.nttdata.project.bank.account.infrastructure.Mapper;

import com.nttdata.project.bank.account.domain.model.BankAccount;
import com.nttdata.project.bank.account.infrastructure.DTO.BankAccountDTO;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BankAccountMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    private BankAccountMapper() {}

    public static BankAccount mapToEntity(final Object bankAccountDTO) {
        return modelMapper.map(bankAccountDTO, BankAccount.class);
    }

    public static BankAccountDTO mapToDTO(final Object bankAccount) {
        return modelMapper.map(bankAccount, BankAccountDTO.class);
    }
}
