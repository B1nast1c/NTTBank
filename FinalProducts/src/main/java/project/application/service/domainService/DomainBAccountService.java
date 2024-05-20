package project.application.service.domainService;

import org.springframework.stereotype.Service;
import project.application.service.BankAccountService;
import project.domain.ports.BAccountPort;
import project.infrastructure.adapters.GenericAccAdapter;
import project.infrastructure.clientcalls.WebClientSrv;
import project.infrastructure.clientcalls.responses.ClientResponse;
import project.infrastructure.dto.BankAccountDTO;
import project.infrastructure.dto.CurrAccDTO;
import project.infrastructure.dto.LegalSignerDTO;
import project.infrastructure.exceptions.CustomError;
import project.infrastructure.factory.BARepoFactory;
import project.infrastructure.mapper.GenericMapper;
import project.infrastructure.responses.CustomResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class DomainBAccountService implements BankAccountService {
  private final BARepoFactory repositoryFactory;
  private final WebClientSrv clientService;
  private final GenericAccAdapter genericRepository;
  private BAccountPort repository;

  public DomainBAccountService(final BARepoFactory repositoryFactory,
                               final WebClientSrv apiCalls,
                               final GenericAccAdapter genericRepository) {
    this.repositoryFactory = repositoryFactory;
    this.clientService = apiCalls;
    this.genericRepository = genericRepository;
  }

  private void setStrategy(String accountType) {
    repository = repositoryFactory.getAdapter(accountType);
  }

  @Override
  public Mono<CustomResponse<Object>> createBankAccount(final Object account) {
    BankAccountDTO bankAccountDTO = GenericMapper.mapToSpecificClass(account, BankAccountDTO.class);
    String clientId = bankAccountDTO.getClientDocument();
    Mono<ClientResponse> foundClientMono = clientService.getClientByiD(clientId);

    return foundClientMono.flatMap(foundClient -> {
      if (foundClient.isSuccess()) {
        setStrategy(bankAccountDTO.getAccountType());
        return repository.save(account, foundClient.getData())
            .map(savedAccount -> new CustomResponse<>(
                true,
                GenericMapper.mapToSpecificClass(savedAccount, Object.class))
            );
      } else {
        Object error = new CustomError(
            "Client retrieval failed, please try again",
            CustomError.ErrorType.POST_ERROR);

        CustomResponse<Object> errorResponse = new CustomResponse<>(false, error);
        return Mono.just(errorResponse); // Casting necesario
      }
    }).onErrorResume(throwable -> {
      Object error = new CustomError(throwable.getMessage(), CustomError.ErrorType.POST_ERROR);
      CustomResponse<Object> errorResponse = new CustomResponse<>(false, error);
      return Mono.just(errorResponse); // Casting necesario
    });
  }


  @Override
  public Mono<CustomResponse<Object>> getBankAccount(String accountNumber) {
    return genericRepository.findByAccountNumber(accountNumber)
        .flatMap(account -> {

          // LOGGER -> System.out.println("FOUND ACCOUNT -> " + account);

          CustomResponse<Object> response = new CustomResponse<>(true, account);
          return Mono.just(response);
        })
        .onErrorResume(
            throwable -> {
              Object error = new CustomError("Account does not exist", CustomError.ErrorType.GET_ERROR);
              CustomResponse<Object> badResponse = new CustomResponse<>(false, error);
              return Mono.just(badResponse);
            }
        );
  }

  @Override
  public Mono<CustomResponse<Object>> updateBankAccount(String accountNumber, BankAccountDTO bankAccountDTO) {
    return genericRepository.findByAccountNumber(accountNumber)
        .flatMap(
            account -> {
              BankAccountDTO tempDTO = GenericMapper.mapToSpecificClass(account, BankAccountDTO.class);
              setStrategy(tempDTO.getAccountType());

              return repository.update(account, bankAccountDTO)
                  .flatMap(result -> {
                    CustomResponse<Object> response = new CustomResponse<>(true, result);
                    return Mono.just(response);
                  });
            }
        )
        .onErrorResume(
            throwable -> {
              Object error = new CustomError(throwable.getMessage(), CustomError.ErrorType.PUT_ERROR);
              CustomResponse<Object> badResponse = new CustomResponse<>(false, error);
              return Mono.just(badResponse);
            }
        );
  }

  @Override
  public Mono<CustomResponse<Object>> deleteBankAccount(String accountNumber) {

    return null;
  }

  @Override
  public Mono<CustomResponse<List<Object>>> getBankAccounts() {
    Flux<Object> accounts = genericRepository.findAll();
    Mono<List<Object>> accountsListMono = accounts.collectList();
    return accountsListMono.map(list -> new CustomResponse<>(true, list));
  }

  @Override
  public Mono<CustomResponse<Object>> getAllBankAccountsByClientId(String clientId) {
    Flux<?> accounts = genericRepository.findByClientId(clientId);
    Mono<? extends List<?>> accountMonoList = accounts.collectList();

    return accountMonoList.flatMap(
            accountList -> {
              CustomResponse<Object> response = new CustomResponse<>(true, accountList);
              return Mono.just(response);
            }
        )
        .onErrorResume(throwable -> {
          CustomError error = new CustomError(throwable.getMessage(), CustomError.ErrorType.GET_ERROR);
          CustomResponse<Object> badResponse = new CustomResponse<>(false, error);
          return Mono.just(badResponse);
        });
  }

  @Override
  public Mono<CustomResponse<CurrAccDTO>> addTitularsToAccount(String accountNumber, List<String> titulars) {
    return null;
  }

  @Override
  public Mono<CustomResponse<CurrAccDTO>> addLegalSignersToAccount(String accountNumber, List<LegalSignerDTO> legalSignersDTO) {
    return null;
  }

  @Override
  public Mono<CustomResponse<CurrAccDTO>> removeTitularfromAccount(String accountNumber, String titularId) {
    return null;
  }

  @Override
  public Mono<CustomResponse<CurrAccDTO>> removeLegalSignerfromAccount(String accountNumber, String signerId) {
    return null;
  }

  @Override
  public Mono<CustomResponse<Object>> getAccountBalance(String accountNumber) {
    return genericRepository.findByAccountNumber(accountNumber)
        .flatMap(account -> {
          BankAccountDTO accountDTO = GenericMapper.mapToSpecificClass(account, BankAccountDTO.class);
          CustomResponse<Object> response = new CustomResponse<>(true, accountDTO.getBalance());
          return Mono.just(response);
        })
        .onErrorResume(
            throwable -> {
              Object error = new CustomError("Account does not exist", CustomError.ErrorType.GET_ERROR);
              CustomResponse<Object> badResponse = new CustomResponse<>(false, error);
              return Mono.just(badResponse);
            }
        );
  }
}