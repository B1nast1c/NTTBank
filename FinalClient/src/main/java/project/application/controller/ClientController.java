package project.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import project.application.service.ClientService;
import project.infrastructure.dto.ClientDTO;
import project.infrastructure.exceptions.CustomResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controlador que funciona como la conexión del cliente y el servicio implementado.
 */
@Slf4j
@RestController
@RequestMapping("/clients")
public class ClientController {
  private final ClientService clientService;

  /**
   * Constructor para el controlador de clientes.
   *
   * @param clientService Servicio de cliente para la lógica de negocio.
   */
  public ClientController(ClientService clientService) {
    this.clientService = clientService;
  }

  /**
   * Recupera todos los clientes almacenados en el sistema.
   *
   * @return Flux que emite DTOs de clientes.
   */
  @GetMapping(value = "/all")
  public Flux<CustomResponse> getClients() {
    return clientService.getAllClients();
  }

  /**
   * Recupera un cliente específico basado en su identificador personalizado.
   *
   * @param documentNumber Identificador personalizado del cliente.
   * @return Mono que emite el DTO del cliente encontrado, o un error si no se encuentra.
   */
  @GetMapping(value = "/{documentNumber}")
  public Mono<CustomResponse> getClientById(@PathVariable String documentNumber) {
    return clientService.getClient(documentNumber);
  }

  /**
   * Agrega un nuevo cliente al sistema.
   *
   * @param clientDto DTO del cliente a agregar.
   * @return Mono que emite un mensaje de confirmación o un error si la operación falla.
   */
  @PostMapping(value = "/create")
  public Mono<CustomResponse> addClient(@RequestBody final ClientDTO clientDto) {
    return clientService.addClient(clientDto);
  }

  /**
   * Actualiza un cliente existente en el sistema.
   *
   * @param customId Identificador personalizado del cliente a actualizar.
   * @param client   Objeto Cliente con los nuevos datos.
   * @return Mono que emite un mensaje de confirmación o un error si la operación falla.
   */
  @PutMapping(value = "/update/{customId}")
  public Mono<CustomResponse> updateClient(@PathVariable("customId") final String customId, @RequestBody final Object client) {
    return clientService.updateClient(customId, client);
  }

  /**
   * Elimina un cliente existente del sistema.
   *
   * @param customId Identificador personalizado del cliente a eliminar.
   * @return Mono que emite un mensaje de confirmación o un error si la operación falla.
   */
  @DeleteMapping(value = "/delete/{customId}")
  public Mono<CustomResponse> deleteClient(@PathVariable("customId") final String customId) {
    return clientService.deleteClient(customId);
  }
}