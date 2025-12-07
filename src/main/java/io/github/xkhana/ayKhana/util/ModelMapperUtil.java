package io.github.xkhana.ayKhana.util;

public interface ModelMapperUtil {
  void map(Object source, Object destination);

  <D> D map(Object source, Class<D> destinationType);
}
