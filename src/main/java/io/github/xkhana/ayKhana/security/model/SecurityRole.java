package io.github.xkhana.ayKhana.security.model;

import io.github.xkhana.ayKhana.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public class SecurityRole implements GrantedAuthority {
  private final Role role;

  @Override
  public String getAuthority() {
    return role.name();
  }
}
