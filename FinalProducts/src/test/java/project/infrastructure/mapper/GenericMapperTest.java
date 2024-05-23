package project.infrastructure.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias para GenericMapper.
 */
class GenericMapperTest {

  private final TestingClass testingObj = new TestingClass("test", "name");
  private final TestingClassAlt testingObjAlt = new TestingClassAlt("test", "name");
  private GenericMapper genericMapper;
  private ModelMapper modelMapper;

  /**
   * Configuración inicial para cada prueba.
   */
  @BeforeEach
  void setUp() {
    modelMapper = mock(ModelMapper.class);
    genericMapper = new GenericMapper(modelMapper);
  }

  /**
   * Verifica si se realiza correctamente el mapeo de una lista a otra lista específica.
   */
  @Test
  void shouldMapListToSpecificList() {
    List<TestingClass> testList = new ArrayList<>();
    TestingClass entity1 = testingObj;
    TestingClass entity2 = testingObj;
    testList.add(entity1);
    testList.add(entity2);
    List<TestingClassAlt> testAltList = new ArrayList<>();
    testAltList.add(new TestingClassAlt());
    testAltList.add(new TestingClassAlt());
    Class<TestingClass> destinationClass = TestingClass.class; // Lista de la clase del elemento final
    when(modelMapper.map(testAltList.get(0), destinationClass)).thenReturn(entity1);
    when(modelMapper.map(testAltList.get(1), destinationClass)).thenReturn(entity2);

    List<TestingClass> resultList = GenericMapper.mapList(testAltList, destinationClass);

    assertEquals(testAltList.size(), resultList.size());
    for (int i = 0; i < testAltList.size(); i++) {
      assertEquals(testAltList.get(i).getName(), resultList.get(i).getName());
    }
  }
}