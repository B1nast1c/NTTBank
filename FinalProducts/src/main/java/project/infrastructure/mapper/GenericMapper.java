package project.infrastructure.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GenericMapper {
  protected static ModelMapper modelMapper = new ModelMapper();

  GenericMapper(ModelMapper modelMapper) {
  }

  public static <T> T mapToSpecificClass(Object account, Class<T> targetClass) {
    return modelMapper.map(account, targetClass);
  }

  public static <T, U> List<U> mapList(List<T> entityList, Class<U> targetClass) {
    return entityList.stream()
        .map(entity -> mapToSpecificClass(entity, targetClass))
        .collect(Collectors.toList());
  }
}