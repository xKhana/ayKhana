package io.github.xkhana.ayKhana.model.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorDetails {
  private String code;
  private List<ValidationError> details;

  public ErrorDetails(String code) {
    this(code, null);
  }
}
