package io.github.xkhana.ayKhana.controller;

import io.github.xkhana.ayKhana.model.request.AccessTokenRequest;
import io.github.xkhana.ayKhana.model.request.CustomerRegistrationRequest;
import io.github.xkhana.ayKhana.model.response.ApiResponse;
import io.github.xkhana.ayKhana.model.response.CustomerLoginResponse;
import io.github.xkhana.ayKhana.model.response.CustomerRegistrationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

  private static final Logger LOG = LoggerFactory.getLogger(AuthenticationController.class);

  private final AuthenticationService authenticationService;

  @PostMapping("/register")
  public ResponseEntity<ApiResponse<CustomerRegistrationResponse>> register(@RequestBody @Valid CustomerRegistrationRequest registrationDto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(
        ApiResponse.success(authenticationService.register(registrationDto))
    );
  }

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<?>> login(Authentication authentication) {
    String accessToken = authenticationService.generateAccessToken(authentication.getName(), authentication.getAuthorities());
    String refreshToken = authenticationService.generateRefreshToken(authentication.getName(), authentication.getAuthorities());

    ResponseCookie cookie = ResponseCookie.from("JWT_ACCESS_TOKEN", accessToken)
        .httpOnly(true)
        .secure(true)
        .sameSite("Lax")
        .maxAge(AuthenticationService.ACCESS_TOKEN_MAX_AGE_SECONDS)
        .build();

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(ApiResponse.success(new CustomerLoginResponse(refreshToken)));
  }

  @PostMapping("/token")
  public ResponseEntity<ApiResponse<?>> token(Authentication authentication, @RequestBody AccessTokenRequest request) {
    if (!authenticationService.validateRefreshToken(authentication, request.getRefreshToken()))
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("INVALID_REFRESH_TOKEN"));

    String accessToken = authenticationService.generateAccessToken(authentication.getName(), authentication.getAuthorities());

    ResponseCookie cookie = ResponseCookie.from("JWT_ACCESS_TOKEN", accessToken)
        .httpOnly(true)
        .secure(true)
        .sameSite("Lax")
        .maxAge(AuthenticationService.ACCESS_TOKEN_MAX_AGE_SECONDS)
        .build();

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(ApiResponse.success());
  }

  @DeleteMapping("/logout")
  public ResponseEntity<ApiResponse<?>> logout(Authentication authentication) {
    authenticationService.logout(authentication);
    return ResponseEntity.ok().body(ApiResponse.success());
  }

}
