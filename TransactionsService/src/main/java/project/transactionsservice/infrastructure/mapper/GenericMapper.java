package project.transactionsservice.infrastructure.mapper;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import project.transactionsservice.domain.model.Transaction;
import project.transactionsservice.infrastructure.dto.TransactionDTO;

/**
 * Mapper funcional para el cambio de clases, uso de modelMapper, se ha decidido hacerla estática
 * Para compartir un mismo elemento de mapping durante el servicio.
 */
@Slf4j
@Component
public class GenericMapper {
  private static final ModelMapper modelMapper = new ModelMapper();

  public static TransactionDTO mapToDto(final Transaction transaction) {
    try {
      return modelMapper.map(transaction, TransactionDTO.class);
    } catch (Exception e) {
      throw new RuntimeException("Failed while mapping");
    }
  }

  public static <T> T mapToAny(final Object genericObject, Class<T> targetClass) {
    return modelMapper.map(genericObject, targetClass);
  }
}
