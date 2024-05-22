package project.infrastructure.adapters;

class SavingsAccAdapterTest {
 /* @Mock
  SavingsRepo savingsrepo;
  @Mock
  SaveDomainValidations saveDomainValidations;
  @Mock
  UpdateDomainValidations updateDomainValidations;
  @Mock
  ReactiveMongoTemplate reactiveMongoTemplate;
  @InjectMocks
  SavingsAccAdapter savingsAccAdapter;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testSave() {
    when(savingsrepo.insert(any(S.class))).thenReturn(null);
    when(saveDomainValidations.validateSavingsAccount(any(Client.class))).thenReturn(null);

    Mono<Object> result = savingsAccAdapter.save("bankAccountDTO", new Client("customId", "clientType", "clientName", "clientAddress", "clientEmail", "clientPhone", "documentNumber", Boolean.TRUE));
    Assertions.assertNull(result);
  }

  @Test
  void testUpdate() {
    when(updateDomainValidations.validateAmmount(any(Object.class))).thenReturn(null);
    when(updateDomainValidations.validateSavingsAccount(any(SavingsDTO.class), any(SavingsAccount.class))).thenReturn(null);
    when(reactiveMongoTemplate.findAndModify(any(Query.class), any(UpdateDefinition.class), any(Class<T>.class))).thenReturn(null);

    Mono<Object> result = savingsAccAdapter.update("foundAccount", "updatedAccount");
    Assertions.assertNull(result);
  }*/
}
