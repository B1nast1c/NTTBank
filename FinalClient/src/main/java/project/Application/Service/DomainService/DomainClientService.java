package project.Application.Service.DomainService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.Application.Service.ClientService;
import project.Domain.Ports.ClientPort;
import project.Infrastructure.DTO.ClientDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DomainClientService implements ClientService {

    @Autowired
    private final ClientPort clientPort;

    public DomainClientService(ClientPort clientPort) {
        this.clientPort = clientPort;
    }

    @Override
    public Mono<ClientDTO> addClient(Mono<ClientDTO> client) {
        return clientPort.save(client);
    }

    @Override
    public Mono<Void> updateClient(String clientId, Mono<ClientDTO> client) {
        client.flatMap(newClient ->
                clientPort.findByID(clientId).flatMap(existingClient -> {
                    existingClient.setCustomId(clientId);
                    existingClient.setClientType(newClient.getClientType());
                    return clientPort.save(Mono.just(existingClient));
                })
        ).then();
        return null; // Posibilidad de modificación de información
    }

    @Override
    public Mono<Void> deleteClient(String clientId) {
        return clientPort.delete(clientId);
    }

    @Override
    public Mono<ClientDTO> getClient(String clientId) {
        return clientPort.findByID(clientId);
    }

    @Override
    public Flux<ClientDTO> getAllClients() {
        return clientPort.findAll();
    }
}
