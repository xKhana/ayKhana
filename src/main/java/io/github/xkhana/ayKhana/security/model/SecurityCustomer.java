package io.github.xkhana.ayKhana.security.model;

import io.github.xkhana.ayKhana.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
public class SecurityCustomer implements UserDetails {
  private final Customer customer;

  @Override
  public String getUsername() {
    return customer.getUsername();
  }

  @Override
  public String getPassword() {
    return customer.getPassword();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return new ArrayList<>();
  }
}
