package io.github.xkhana.ayKhana.model.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ValidationError {
  private String code;
  private String field;
}