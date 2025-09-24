package io.github.xkhana.ayKhana.service;

import io.github.xkhana.ayKhana.controller.AuthenticationService;
import io.github.xkhana.ayKhana.entity.Customer;
import io.github.xkhana.ayKhana.entity.RefreshToken;
import io.github.xkhana.ayKhana.exception.BusinessException;
import io.github.xkhana.ayKhana.model.request.CustomerRegistrationRequest;
import io.github.xkhana.ayKhana.model.response.CustomerRegistrationResponse;
import io.github.xkhana.ayKhana.repository.CustomerRepository;
import io.github.xkhana.ayKhana.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private static final Logger LOG = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

  private final JwtEncoder encoder;
  private final JwtDecoder decoder;
  private final PasswordEncoder passwordEncoder;
  private final RefreshTokenRepository refreshTokenRepository;
  private final CustomerRepository customerRepository;
  private final ModelMapper modelMapper;

  @Override
  @Transactional
  public CustomerRegistrationResponse register(CustomerRegistrationRequest customer) {
    if (!customer.getPassword().equals(customer.getPasswordConfirmation()))
      throw new BusinessException("PASSWORD_MISMATCH", "Password confirmation does not match password");
    if (customerRepository.findByUsername(customer.getUsername()).isPresent())
      throw new BusinessException("USERNAME_EXISTS", "Username already exists");

    customer.setPassword(passwordEncoder.encode(customer.getPassword()));
    Customer usr = modelMapper.map(customer, Customer.class);
    customerRepository.save(usr);

    RefreshToken refreshToken = new RefreshToken(usr, generateJwtToken(usr.getUsername(), new ArrayList<>(), REFRESH_TOKEN_MAX_AGE_SECONDS));
    refreshTokenRepository.save(refreshToken);

    return modelMapper.map(customer, CustomerRegistrationResponse.class);
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
  public boolean validateRefreshToken(Authentication authentication, String refreshToken) {
    if (refreshTokenRepository.findByToken(refreshToken).orElse(null) == null)
      return false;

    Map<String, Object> claims = decoder.decode(refreshToken).getClaims();
    return claims.get("sub").equals(authentication.getName()) &&
        claims.get("scope").equals(
            authentication.getAuthorities().stream()
                .sorted()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "))
        ) &&
        Instant.now().isBefore((Instant) claims.get("exp"));
  }

  @Override
  public boolean logout(Authentication authentication) {
    return updateRefreshToken(authentication.getName(), null);
  }

  private String generateJwtToken(String username, Collection<? extends GrantedAuthority> authorities, Long maxAgeInSeconds) {
    Instant now = Instant.now();
    String scope = authorities.stream()
        .sorted()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(" "));
    JwtClaimsSet claims = JwtClaimsSet.builder()
        .issuer("self")
        .issuedAt(now)
        .expiresAt(now.plusSeconds(maxAgeInSeconds))
        .subject(username)
        .claim("scope", scope)
        .build();
    return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
  }

  private boolean updateRefreshToken(String username, String refreshToken) {
    int rowCount = refreshTokenRepository.updateTokenByUsername(username, refreshToken);
    if (rowCount == 1)
      return true;
    else if (rowCount == 0)
      throw new BusinessException("REFRESH_TOKEN_NOT_FOUND", "Refresh token not found");

    throw new BusinessException("REFRESH_TOKEN_UPDATE_FAILED", "Error while updating refresh token");
  }

}
