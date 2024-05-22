package project.application.controller;

class BankAccountControllerTest {
  /*
  @Mock
  BankAccountService bankAccountService;
  @InjectMocks
  BankAccountController bankAccountController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testAddBankAccount() {
    when(bankAccountService.createBankAccount(any(Object.class))).thenReturn(null);

    Mono<CustomResponse<Object>> result = bankAccountController.addBankAccount("bankAccount");
    Assertions.assertNull(result);
  }

  @Test
  void testGetBankAccountsByClientId() {
    when(bankAccountService.getAllBankAccountsByClientId(anyString())).thenReturn(null);

    Mono<CustomResponse<Object>> result = bankAccountController.getBankAccountsByClientId("clientId");
    Assertions.assertNull(result);
  }

  @Test
  void testGetBankAccountsById() {
    when(bankAccountService.getBankAccount(anyString())).thenReturn(null);

    Mono<CustomResponse<Object>> result = bankAccountController.getBankAccountsById("accountId");
    Assertions.assertNull(result);
  }

  @Test
  void testGetBankAccountBalance() {
    when(bankAccountService.getAccountBalance(anyString())).thenReturn(null);

    Mono<CustomResponse<Object>> result = bankAccountController.getBankAccountBalance("accountId");
    Assertions.assertNull(result);
  }

  @Test
  void testGetAllBankAccounts() {
    when(bankAccountService.getBankAccounts()).thenReturn(null);

    Mono<CustomResponse<List<Object>>> result = bankAccountController.getAllBankAccounts();
    Assertions.assertNull(result);
  }

  @Test
  void testUpdateBankAccount() {
    when(bankAccountService.updateBankAccount(anyString(), any(BankAccountDTO.class))).thenReturn(null);

    Mono<CustomResponse<Object>> result = bankAccountController.updateBankAccount("accountId", new BankAccountDTO("id", "clientDocument", "accountNumber", 0d, 0, "accountType"));
    Assertions.assertNull(result);
  }

  @Test
  void testAddTitularsToAccount() {
    when(bankAccountService.addTitularsToAccount(anyString(), any(List<String>.class))).thenReturn(null);

    Mono<CustomResponse<CurrAccDTO>> result = bankAccountController.addTitularsToAccount("accountId", List.of("titulars"));
    Assertions.assertNull(result);
  }

  @Test
  void testRemoveTitulars() {
    when(bankAccountService.removeTitularfromAccount(anyString(), anyString())).thenReturn(null);

    Mono<CustomResponse<CurrAccDTO>> result = bankAccountController.removeTitulars("accountId", "ownerId");
    Assertions.assertNull(result);
  }

  @Test
  void testAddSignersToAccount() {
    when(bankAccountService.addLegalSignersToAccount(anyString(), any(List<LegalSignerDTO>.class))).thenReturn(null);

    Mono<CustomResponse<CurrAccDTO>> result = bankAccountController.addSignersToAccount("accountId", List.of(new LegalSignerDTO("signerNumber", "name", "lastName")));
    Assertions.assertNull(result);
  }

  @Test
  void testRemoveLegalSigners() {
    when(bankAccountService.removeLegalSignerfromAccount(anyString(), anyString())).thenReturn(null);

    Mono<CustomResponse<CurrAccDTO>> result = bankAccountController.removeLegalSigners("accountId", "ownerId");
    Assertions.assertNull(result);
  }*/
}