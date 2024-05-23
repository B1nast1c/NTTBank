package project.infrastructure.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import project.domain.ports.BAccountPort;
import project.infrastructure.adapters.CurrAccAdapter;
import project.infrastructure.adapters.FxdTermAdapter;
import project.infrastructure.adapters.SavingsAccAdapter;
import project.infrastructure.exceptions.throwable.WrongAccountType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Pruebas unitarias para BARepoFactory.
 */
class BARepoFactoryTest {
  private BARepoFactory baRepoFactory;

  @Mock
  private SavingsAccAdapter savingsAccAdapter;

  @Mock
  private FxdTermAdapter fxdTermAdapter;

  @Mock
  private CurrAccAdapter currAccAdapter;

  /**
   * Configuración inicial para cada prueba.
   */
  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    baRepoFactory = new BARepoFactory(savingsAccAdapter, fxdTermAdapter, currAccAdapter);
  }

  /**
   * Verifica si se asigna correctamente el adaptador de cuenta de ahorro.
   */
  @Test
  void shouldAssignAhorro() {
    String type = "AHORRO";

    BAccountPort adapter = baRepoFactory.getAdapter(type);

    assertEquals(savingsAccAdapter, adapter);
  }

  /**
   * Verifica si se asigna correctamente el adaptador de cuenta de plazo fijo.
   */
  @Test
  void shouldAssignPlazoFijo() {
    String type = "PLAZO_FIJO";

    BAccountPort adapter = baRepoFactory.getAdapter(type);

    assertEquals(fxdTermAdapter, adapter);
  }

  /**
   * Verifica si se asigna correctamente el adaptador de cuenta corriente.
   */
  @Test
  void shouldAssignCuentaCorriente() {
    String type = "CUENTA_CORRIENTE";

    BAccountPort adapter = baRepoFactory.getAdapter(type);

    assertEquals(currAccAdapter, adapter);
  }

  /**
   * Verifica si se lanza una excepción al intentar asignar un tipo de cuenta incorrecto.
   */
  @Test
  void shouldNotAssign() {
    String type = "RANDOM";

    assertThrows(WrongAccountType.class, () -> baRepoFactory.getAdapter(type));
  }
}