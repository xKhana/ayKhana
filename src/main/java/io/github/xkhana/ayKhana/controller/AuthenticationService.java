package io.github.xkhana.ayKhana.controller;

import io.github.xkhana.ayKhana.model.request.UserRegistrationRequest;
import io.github.xkhana.ayKhana.model.response.UserRegistrationResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;


public interface AuthenticationService {
  Long ACCESS_TOKEN_MAX_AGE_SECONDS = 20 * 60L;
  Long REFRESH_TOKEN_MAX_AGE_SECONDS = 7 * 24 * 60 * 60L;

  String generateAccessToken(String username, Collection<? extends GrantedAuthority> authorities);

  String generateRefreshToken(String username, Collection<? extends GrantedAuthority> authorities);

  Jwt validateRefreshToken(String refreshToken);

  boolean logout(Authentication authentication);

  UserRegistrationResponse register(UserRegistrationRequest registrationDto);
}
