package project.Infrastructure.Mapper;

import org.modelmapper.ModelMapper;
import project.Domain.Model.Client;
import project.Domain.Model.ClientType;
import project.Infrastructure.DTO.ClientDTO;
import project.Infrastructure.Exceptions.CustomException;

public class GenericMapper {
  private static final ModelMapper modelMapper = new ModelMapper();

  public static Client mapToEntity(ClientDTO clientDTO) {
    try {
      Client client = modelMapper.map(clientDTO, Client.class);
      client.setClientType(ClientType.valueOf(clientDTO.getClientType()));
      return modelMapper.map(clientDTO, Client.class);
    } catch (Exception e) {
      throw new CustomException("Type must be PERSONAL or EMPRESARIAL");
    }
  }

  public static ClientDTO mapToDto(Client client) {
    return modelMapper.map(client, ClientDTO.class);
  }
}
