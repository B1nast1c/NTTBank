package project.Infrastructure.Controller;

import org.springframework.web.bind.annotation.*;
import project.Application.Service.ClientService;
import project.Infrastructure.DTO.ClientDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping(value = "/all")
    public Flux<ClientDTO> getClients() {
        return clientService.getAllClients();
    }

    @GetMapping(value = "/{customId}")
    public Mono<ClientDTO> getClient(@PathVariable("customId") String customId) {
        return clientService.getClient(customId);
    }

    @PostMapping(value = "/create")
    public Mono<ClientDTO> addClient(@RequestBody Mono<ClientDTO> clientDto) {
        return clientService.addClient(clientDto);
    }

    @PutMapping(value = "/update/{customId}")
    public Mono<Void> updateClient(@PathVariable("customId") String customId, @RequestBody Mono<ClientDTO> client) {
        return clientService.updateClient(customId, client);
    }

    @DeleteMapping(value = "/delete/{customId}")
    public Mono<Void> deleteClient(@PathVariable("customId") String customId) {
        return clientService.deleteClient(customId);
    }
}
