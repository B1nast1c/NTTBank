package project.Infrastructure.Adapters.MongoRepo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import project.Domain.Model.Client;

public interface ClientRepository extends ReactiveMongoRepository<Client, String> {
}