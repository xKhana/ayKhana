package io.github.xkhana.ayKhana.controller;

import io.github.xkhana.ayKhana.entity.Role;
import io.github.xkhana.ayKhana.model.request.AccessTokenRequest;
import io.github.xkhana.ayKhana.model.request.UserRegistrationRequest;
import io.github.xkhana.ayKhana.model.response.ApiResponse;
import io.github.xkhana.ayKhana.model.response.UserLoginResponse;
import io.github.xkhana.ayKhana.model.response.UserRegistrationResponse;
import io.github.xkhana.ayKhana.security.model.SecurityRole;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;


@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
  private final AuthenticationService authenticationService;

  @PostMapping("/register")
  public ResponseEntity<ApiResponse<UserRegistrationResponse>> register(@RequestBody @Valid UserRegistrationRequest registrationDto) {
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
        .body(ApiResponse.success(new UserLoginResponse(refreshToken)));
  }

  @PostMapping("/token")
  public ResponseEntity<ApiResponse<?>> token(@RequestBody AccessTokenRequest request) {
    Jwt refreshToken = authenticationService.validateRefreshToken(request.getRefreshToken());

    String accessToken = authenticationService.generateAccessToken(
        refreshToken.getSubject(),
        Collections.singleton(
            new SecurityRole(
                Role.valueOf(refreshToken.getClaim("role"))
            )
        )
    );

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
