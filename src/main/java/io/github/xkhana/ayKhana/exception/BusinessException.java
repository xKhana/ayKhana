package io.github.xkhana.ayKhana.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BusinessException extends RuntimeException {
  private final String code;

  public BusinessException(String code, String message) {
    super(message);
    this.code = code;
  }
}
