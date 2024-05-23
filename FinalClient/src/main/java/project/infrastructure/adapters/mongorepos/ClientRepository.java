package project.infrastructure.adapters.mongorepos;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import project.domain.model.Client;
import reactor.core.publisher.Mono;

/**
 * Repositorio de Spring-Data MongoDB para la entidad Cliente
 */
public interface ClientRepository extends ReactiveMongoRepository<Client, String> {
  /**
   * Verifica si existe un cliente con un ID personalizado.
   *
   * @param customId ID personalizado del cliente a buscar.
   * @return Un Mono que emite `true` si el cliente existe, o `false` si no.
   */
  Mono<Boolean> existsByCustomId(String customId);

  /**
   * Busca un cliente por su número de documento.
   *
   * @param documentNumber Número de documento del cliente a buscar.
   * @return Un Mono que emite el cliente encontrado o está vacío si no se encuentra.
   */
  Mono<Client> findByDocumentNumber(String documentNumber);

  /**
   * Verifica si existe un cliente con un número de documento específico.
   *
   * @param documentNumber Número de documento del cliente a buscar.
   * @return Un Mono que emite `true` si el cliente existe, o `false` si no.
   */
  Mono<Boolean> existsByDocumentNumber(String documentNumber);
}
