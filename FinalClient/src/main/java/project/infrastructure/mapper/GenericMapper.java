package project.infrastructure.mapper;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import project.domain.model.Client;
import project.infrastructure.dto.ClientDTO;
import project.infrastructure.exceptions.CustomError;
import project.infrastructure.exceptions.throwable.EmptyAttributes;

/**
 * Mapper funcional para el cambio de clases, uso de modelMapper, se ha decidido hacerla estática
 * Para compartir un mismo elemento de mapping durante todo el servicio.
 */
@Slf4j
public class GenericMapper {
  private static final ModelMapper modelMapper = new ModelMapper();

  private GenericMapper() {
  }

  /**
   * Mapping entre DTO -> Cliente
   *
   * @param clientDTO
   * @return Cliente (Entidad del modelo)
   */
  public static Client mapToEntity(final Object clientDTO) {
    try {
      return modelMapper.map(clientDTO, Client.class);
    } catch (Exception e) {
      log.error("DNI or NAME are missing -> {}", CustomError.ErrorType.WRONG_PARAMS);
      throw new EmptyAttributes("Some attributes were not set");
    }
  }

  /**
   * Mapeo Cliente -> DTO
   *
   * @param client Objeto del modelo Cliente
   * @return DTO
   */
  public static ClientDTO mapToDto(final Object client) {
    try {
      return modelMapper.map(client, ClientDTO.class);
    } catch (Exception e) {
      log.error("Some attributes are missing -> {}", CustomError.ErrorType.WRONG_PARAMS);
      throw new EmptyAttributes("Some attributes were not set");
    }
  }
}
