package project.transactionsservice.infrastructure.mapper;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import project.transactionsservice.domain.model.Transaction;
import project.transactionsservice.infrastructure.dto.TransactionDTO;

/**
 * Mapper funcional para el cambio de clases, uso de modelMapper, se ha decidido hacerla estática
 * Para compartir un mismo elemento de mapping durante todo el servicio.
 */
@Slf4j
@Component
public class GenericMapper {
  private static final ModelMapper modelMapper = new ModelMapper();

  private GenericMapper() {
  }

  public static Transaction mapToEntity(final Object transactionDTO) {
    try {
      return modelMapper.map(transactionDTO, Transaction.class);
    } catch (Exception e) {
      throw new RuntimeException("");
    }
  }

  public static TransactionDTO mapToDto(final Object transaction) {
    try {
      return modelMapper.map(transaction, TransactionDTO.class);
    } catch (Exception e) {
      throw new RuntimeException("");
    }
  }
}
