package project.infrastructure.adapters;

import org.springframework.stereotype.Repository;
import project.domain.model.Account.CurrentAccount;
import project.domain.model.Account.LegalSigner;
import project.domain.model.Account.Titular;
import project.domain.ports.BAccountPort;
import project.infrastructure.adapters.MongoRepo.*;
import project.infrastructure.clientCalls.responses.Client;
import project.infrastructure.dto.account.CurrAccDTO;
import project.infrastructure.exceptions.CustomException;
import project.infrastructure.mapper.GenericMapper;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class CurrAccAdapter implements BAccountPort {
  private final CurrAccRepo currentRepo;
  private final SavingsRepo savingsrepo;
  private final FxdTermRepo fxdtermrepo;
  private final TitularsRepo titularsrepo;
  private final LSignersRepo lsignersrepo;

  public CurrAccAdapter(CurrAccRepo currentRepo, SavingsRepo savingsrepo, FxdTermRepo fxdtermrepo, TitularsRepo titularsrepo, LSignersRepo lsignersrepo) {
    this.currentRepo = currentRepo;
    this.savingsrepo = savingsrepo;
    this.fxdtermrepo = fxdtermrepo;
    this.titularsrepo = titularsrepo;
    this.lsignersrepo = lsignersrepo;
  }

  private CurrentAccount convertClass(Object bankAccountDTO) {
    return GenericMapper.mapToSpecificClass(bankAccountDTO, CurrentAccount.class);
  }

  private CurrAccDTO customDTO(Object currAccount) {
    return GenericMapper.mapToSpecificClass(currAccount, CurrAccDTO.class);
  }

  public Mono<Boolean> isPersonalValidated(Client client) {
    Mono<Boolean> hasSavings = savingsrepo.existsByClientId(client.getCustomId());
    Mono<Boolean> hasCurrent = currentRepo.existsByClientId(client.getCustomId());
    Mono<Boolean> hasFxdTerm = fxdtermrepo.existsByClientId(client.getCustomId());
    return hasSavings.or(hasCurrent).or(hasFxdTerm);
  }

  private Mono<Boolean> isEnterpriseValidated(Client client, CurrAccDTO currAccount) {
    return Mono.just(client.getClientType().equals("EMPRESARIAL") && !currAccount.getAccountTitulars().isEmpty());
  }

  @Override
  public Mono<CurrAccDTO> save(Object bankAccountDTO, Client client) {
    CurrAccDTO dto = customDTO(bankAccountDTO);
    Mono<Boolean> personalValidated = isPersonalValidated(client);
    Mono<Boolean> enterpriseValidated = isEnterpriseValidated(client, dto);

    return Mono.zip(personalValidated, enterpriseValidated)
            .flatMap(tuple -> {
              boolean notValid = tuple.getT1();
              boolean isBusinessValidated = tuple.getT2();

              if (!notValid || isBusinessValidated) { // PENDIENTE LOGICA MANY TO MANY
                List<Titular> titulars = GenericMapper.mapList(dto.getAccountTitulars(), Titular.class);
                List<LegalSigner> legalSigners = GenericMapper.mapList(dto.getLegalSigners(), LegalSigner.class);

                CurrentAccount currentAccount = convertClass(bankAccountDTO);
                currentAccount.setAccountTitulars(titulars);
                currentAccount.setLegalSigners(legalSigners);

                Mono<CurrentAccount> savedAccount = currentRepo.insert(currentAccount);
                titularsrepo.saveAll(titulars);
                lsignersrepo.saveAll(legalSigners);

                return savedAccount
                        .map(this::customDTO);
              } else {
                return Mono.error(new CustomException("Account creation is not allowed"));
              }
            });
  }


}
