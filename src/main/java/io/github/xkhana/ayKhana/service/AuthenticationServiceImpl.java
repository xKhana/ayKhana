package io.github.xkhana.ayKhana.service;

import io.github.xkhana.ayKhana.controller.AuthenticationService;
import io.github.xkhana.ayKhana.entity.RefreshToken;
import io.github.xkhana.ayKhana.entity.Role;
import io.github.xkhana.ayKhana.entity.User;
import io.github.xkhana.ayKhana.exception.BusinessException;
import io.github.xkhana.ayKhana.model.request.UserRegistrationRequest;
import io.github.xkhana.ayKhana.model.response.UserRegistrationResponse;
import io.github.xkhana.ayKhana.repository.RefreshTokenRepository;
import io.github.xkhana.ayKhana.repository.UserRepository;
import io.github.xkhana.ayKhana.util.ModelMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
  private final JwtEncoder encoder;
  private final JwtDecoder decoder;
  private final PasswordEncoder passwordEncoder;
  private final RefreshTokenRepository refreshTokenRepository;
  private final UserRepository userRepository;
  private final ModelMapperUtil modelMapper;

  @Override
  @Transactional
  public UserRegistrationResponse register(UserRegistrationRequest user) {
    if (!user.getPassword().equals(user.getPasswordConfirmation()))
      throw new BusinessException("PASSWORD_MISMATCH", "Password confirmation does not match password",
          HttpStatus.BAD_REQUEST);
    if (userRepository.findByUsername(user.getUsername()).isPresent())
      throw new BusinessException("USERNAME_EXISTS", "Username already exists", HttpStatus.CONFLICT);

    user.setPassword(passwordEncoder.encode(user.getPassword()));
    User usr = modelMapper.map(user, User.class);
    usr.setRole(Role.USER);
    userRepository.save(usr);

    RefreshToken refreshToken = new RefreshToken(usr,
        generateJwtToken(usr.getUsername(), new ArrayList<>(), REFRESH_TOKEN_MAX_AGE_SECONDS));
    refreshTokenRepository.save(refreshToken);

    return modelMapper.map(user, UserRegistrationResponse.class);
  }

  @Override
  public String generateAccessToken(String username, Collection<? extends GrantedAuthority> authorities) {
    return generateJwtToken(username, authorities, ACCESS_TOKEN_MAX_AGE_SECONDS);
  }

  @Override
  public String generateRefreshToken(String username, Collection<? extends GrantedAuthority> authorities) {
    String refreshToken = generateJwtToken(username, authorities, REFRESH_TOKEN_MAX_AGE_SECONDS);

    updateRefreshToken(username, refreshToken);

    return refreshToken;
  }

  @Override
  public Jwt validateRefreshToken(String refreshToken) {
    Jwt jwt;
    try {
      jwt = decoder.decode(refreshToken);
    } catch (JwtException e) {
      throw new BusinessException(
          "INVALID_TOKEN",
          "Invalid refresh token",
          HttpStatus.UNAUTHORIZED);
    }

    refreshTokenRepository.findByToken(refreshToken).orElseThrow(() -> new BusinessException(
        "TOKEN_NOT_FOUND",
        "Refresh token not found",
        HttpStatus.UNAUTHORIZED));

    if (jwt.getExpiresAt() == null || Instant.now().isAfter(jwt.getExpiresAt()))
      throw new BusinessException(
          "TOKEN_EXPIRED",
          "Refresh token has expired",
          HttpStatus.UNAUTHORIZED);

    return jwt;
  }

  @Override
  public boolean logout(Authentication authentication) {
    return updateRefreshToken(authentication.getName(), null);
  }

  private String generateJwtToken(String username, Collection<? extends GrantedAuthority> authorities,
      Long maxAgeInSeconds) {
    Instant now = Instant.now();
    String role = authorities.stream()
        .sorted()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(" "));
    JwtClaimsSet claims = JwtClaimsSet.builder()
        .issuer("ayKhana")
        .issuedAt(now)
        .expiresAt(now.plusSeconds(maxAgeInSeconds))
        .subject(username)
        .claim("role", role)
        .build();
    return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
  }

  private boolean updateRefreshToken(String username, String refreshToken) {
    int rowCount = refreshTokenRepository.updateTokenByUsername(username, refreshToken);
    if (rowCount == 1)
      return true;
    else if (rowCount == 0)
      throw new BusinessException("REFRESH_TOKEN_NOT_FOUND", "Refresh token not found", HttpStatus.NOT_FOUND);

    throw new BusinessException("REFRESH_TOKEN_UPDATE_FAILED", "Error while updating refresh token",
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
