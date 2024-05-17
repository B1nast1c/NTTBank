package project.infrastructure.mapper;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import project.domain.model.Client;
import project.infrastructure.dto.ClientDTO;

/**
 * Mapper genérico para la entidad única que es Cliente.
 */
@Slf4j
public class GenericMapper {
  private static final ModelMapper modelMapper = new ModelMapper();

  /**
   * Mapeo DTO -> Cliente
   *
   * @param clientDTO DTO del cliente
   * @return Cliente
   */
  public static Client mapToEntity(final Object clientDTO) {
    log.info("Mapping clientDTO to Client");
    return modelMapper.map(clientDTO, Client.class);
  }

  /**
   * Mapeo Cliente -> DTO
   *
   * @param client Objeto del modelo Cliente
   * @return DTO del cliente
   */
  public static ClientDTO mapToDto(final Object client) {
    log.info("Mapping client to ClientDTO");
    return modelMapper.map(client, ClientDTO.class);
  }
}
