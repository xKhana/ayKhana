package io.github.xkhana.ayKhana.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AccessTokenRequest {
  @NotEmpty
  private String refreshToken;
}
