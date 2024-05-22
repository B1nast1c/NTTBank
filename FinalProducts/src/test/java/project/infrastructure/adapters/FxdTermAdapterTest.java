package project.infrastructure.adapters;

class FxdTermAdapterTest {
  /*@Mock
  FxdTermRepo fxdTermRepo;
  @Mock
  SaveDomainValidations saveDomainValidations;
  @Mock
  UpdateDomainValidations updateDomainValidations;
  @Mock
  ReactiveMongoTemplate reactiveMongoTemplate;
  @InjectMocks
  FxdTermAdapter fxdTermAdapter;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testSave() {
    when(fxdTermRepo.insert(any(S.class))).thenReturn(null);
    when(saveDomainValidations.validateFxdTermAccount(any(Client.class))).thenReturn(null);

    Mono<Object> result = fxdTermAdapter.save("bankAccountDTO", new Client("customId", "clientType", "clientName", "clientAddress", "clientEmail", "clientPhone", "documentNumber", Boolean.TRUE));
    Assertions.assertNull(result);
  }

  @Test
  void testUpdate() {
    when(updateDomainValidations.validateAmmount(any(Object.class))).thenReturn(null);
    when(reactiveMongoTemplate.findAndModify(any(Query.class), any(UpdateDefinition.class), any(Class<T>.class))).thenReturn(null);

    Mono<Object> result = fxdTermAdapter.update("foundAccount", "updatedAccount");
    Assertions.assertNull(result);
  }*/
}