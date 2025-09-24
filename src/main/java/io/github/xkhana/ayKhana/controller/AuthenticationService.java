package io.github.xkhana.ayKhana.controller;

import io.github.xkhana.ayKhana.model.request.CustomerRegistrationRequest;
import io.github.xkhana.ayKhana.model.response.CustomerRegistrationResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


public interface AuthenticationService {
  Long ACCESS_TOKEN_MAX_AGE_SECONDS = 20 * 60L;
  Long REFRESH_TOKEN_MAX_AGE_SECONDS = 7 * 24 * 60 * 60L;

  String generateAccessToken(String username, Collection<? extends GrantedAuthority> authorities);

  String generateRefreshToken(String username, Collection<? extends GrantedAuthority> authorities);

  boolean validateRefreshToken(Authentication authentication, String refreshToken);

  boolean logout(Authentication authentication);

  CustomerRegistrationResponse register(CustomerRegistrationRequest registrationDto);
}
