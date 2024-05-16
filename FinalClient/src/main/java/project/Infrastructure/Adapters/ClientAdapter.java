package project.Infrastructure.Adapters;

import org.springframework.stereotype.Repository;
import project.Domain.Model.Client;
import project.Domain.Ports.ClientPort;
import project.Infrastructure.Adapters.MongoRepo.ClientRepository;
import project.Infrastructure.DTO.ClientDTO;
import project.Infrastructure.Exceptions.CustomException;
import project.Infrastructure.Mapper.GenericMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public class ClientAdapter implements ClientPort {
  private final ClientRepository clientRepository;

  public ClientAdapter(ClientRepository clientRepository) {
    this.clientRepository = clientRepository;
  }

  @Override
  public Mono<ClientDTO> findByID(String id) {
    return clientRepository.findById(id)
            .flatMap(client -> {
              Optional<Client> optionalClient = Optional.ofNullable(client);
              return Mono.justOrEmpty(optionalClient)
                      .map(GenericMapper::mapToDto);
            });
  }

  @Override
  public Mono<ClientDTO> save(Mono<ClientDTO> client) {
    Mono<ClientDTO> savedDTO;
    try {
      savedDTO = client.map(GenericMapper::mapToEntity)
              .flatMap(clientRepository::insert)
              .map(GenericMapper::mapToDto);
    } catch (Exception e) {
      return Mono.error(new CustomException("Type must be PERSONAL or EMPRESARIAL"));
    }
    return savedDTO;
  }

  @Override
  public Flux<ClientDTO> findAll() {
    return clientRepository.findAll().map(GenericMapper::mapToDto);
  }

  @Override
  public Mono<Void> delete(String clientId) {
    return clientRepository.deleteById(clientId);
  }
}
