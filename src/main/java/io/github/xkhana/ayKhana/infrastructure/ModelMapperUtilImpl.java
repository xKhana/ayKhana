package io.github.xkhana.ayKhana.infrastructure;

import io.github.xkhana.ayKhana.util.ModelMapperUtil;
import org.modelmapper.ModelMapper;

public class ModelMapperUtilImpl implements ModelMapperUtil {

  private final ModelMapper modelMapper;

  public ModelMapperUtilImpl() {
    modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setSkipNullEnabled(true);
  }

  @Override
  public void map(Object source, Object destination) {
    modelMapper.map(source, destination);
  }

  @Override
  public <D> D map(Object source, Class<D> destinationType) {
    return modelMapper.map(source, destinationType);
  }
}
