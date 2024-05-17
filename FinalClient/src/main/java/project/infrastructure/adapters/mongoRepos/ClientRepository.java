package project.infrastructure.adapters.mongoRepos;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import project.domain.model.Client;
import reactor.core.publisher.Mono;

/**
 * Repositorio de Spring Data MongoDB para la entidad Cliente
 */
public interface ClientRepository extends ReactiveMongoRepository<Client, String> {

  /**
   * Comprueba si existe un cliente con el identificador personalizado dado
   *
   * @param customId el identificador personalizado del cliente
   * @return un Mono que indica si existe un cliente con el identificador personalizado dado
   */
  Mono<Boolean> existsByCustomId(String customId);

  /**
   * Encuentra un cliente por el número de documento
   *
   * @param documentNumber el número de documento del cliente
   * @return un Mono que emite el cliente encontrado, o vacío si no se encuentra
   */
  Mono<Client> findByDocumentNumber(String documentNumber);
}
