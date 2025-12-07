package io.github.xkhana.ayKhana.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserLoginResponse {
  private String refreshToken;
}
