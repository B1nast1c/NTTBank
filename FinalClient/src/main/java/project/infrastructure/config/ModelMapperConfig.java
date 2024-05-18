package project.infrastructure.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración para el uso del ModelMapper.
 */
@Configuration
public class ModelMapperConfig {

  /**
   * Crea y configura un objeto ModelMapper.
   *
   * @return El objeto ModelMapper configurado.
   */
  @Bean
  public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setSkipNullEnabled(true);
    return modelMapper;
  }
}
