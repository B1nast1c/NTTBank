package project.infrastructure.adapters;

import org.springframework.stereotype.Repository;
import project.infrastructure.adapters.MongoRepo.BARepo;
import project.infrastructure.dto.account.BankAccountDTO;
import project.infrastructure.mapper.GenericMapper;
import reactor.core.publisher.Flux;

@Repository
public class GenericAccAdapter {
  private final BARepo accountRepository;

  public GenericAccAdapter(BARepo accountRepository) {
    this.accountRepository = accountRepository;
  }

  public Flux<?> findAll() {
    return accountRepository.findAll().map(account -> GenericMapper.mapToSpecificClass(account, BankAccountDTO.class));
  }

  public Flux<?> findByClientId(final String clientId) {
    return accountRepository.findAllByClientId(clientId).map(account -> GenericMapper.mapToSpecificClass(account, BankAccountDTO.class));
  }
}
