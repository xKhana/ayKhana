package io.github.xkhana.ayKhana.exception;

import io.github.xkhana.ayKhana.model.error.ValidationError;
import io.github.xkhana.ayKhana.model.response.ApiResponse;
import io.jsonwebtoken.JwtException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.security.auth.login.AccountLockedException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(AuthorizationDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ResponseEntity<ApiResponse<Object>> handleAuthorizationDeniedException(
      AuthorizationDeniedException ex) {

    log.warn("Authorization denied: {}", ex.getMessage());

    ApiResponse<Object> response = ApiResponse.error(
        "AUTHORIZATION_DENIED"
    );

    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ApiResponse<?>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {

    List<ValidationError> errors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(error -> new ValidationError(
            error.getDefaultMessage(),
            error.getField()
        ))
        .collect(Collectors.toList());

    log.warn("Validation failed: {}", errors);

    ApiResponse<?> response = ApiResponse.validationError(errors);
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ApiResponse<Object>> handleConstraintViolationException(
      ConstraintViolationException ex) {

    List<ValidationError> errors = ex.getConstraintViolations()
        .stream()
        .map(violation -> new ValidationError(
            violation.getMessage(),
            violation.getPropertyPath().toString()
        ))
        .collect(Collectors.toList());

    log.warn("Constraint violation: {}", errors);

    ApiResponse<Object> response = ApiResponse.validationError(errors);
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ResponseEntity<ApiResponse<Object>> handleUsernameNotFoundException(
      UsernameNotFoundException ex) {

    log.warn("Authentication failed: {}", ex.getMessage());

    ApiResponse<Object> response = ApiResponse.error(
        "INVALID_CREDENTIALS"
    );
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
  }

  @ExceptionHandler(BadCredentialsException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ResponseEntity<ApiResponse<Object>> handleBadCredentialsException(
      BadCredentialsException ex) {

    log.warn("Authentication failed: {}", ex.getMessage());

    ApiResponse<Object> response = ApiResponse.error(
        "INVALID_CREDENTIALS"
    );
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
  }

  @ExceptionHandler(InsufficientAuthenticationException.class)
  public ResponseEntity<ApiResponse<?>> handleInsufficientAuth(InsufficientAuthenticationException e) {
    ApiResponse<?> response = ApiResponse.error("INVALID_CREDENTIALS");

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
  }

  @ExceptionHandler(JwtException.class)
  public ResponseEntity<ApiResponse<Object>> handleJwtException(JwtException e) {
    ApiResponse<Object> response = ApiResponse.error("INVALID_OR_EXPIRED_JWT");

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolationException(
      DataIntegrityViolationException ex) {

    String code = "DATA_INTEGRITY_VIOLATION";

    if (ex.getMessage().contains("(phone)"))
      code = "PHONE_NUMBER_ALREADY_EXISTS";
    else if (ex.getMessage().contains("(email)"))
      code = "EMAIL_ALREADY_EXISTS";

    log.warn("Data integrity violation: {}", ex.getMessage());

    ApiResponse<Object> response = ApiResponse.error(code);
    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ApiResponse<Object>> handleBusinessException(
      BusinessException ex) {

    log.warn("Error Message: {}", ex.getMessage());

    ApiResponse<Object> response = ApiResponse.error(
        ex.getCode()
    );
    return ResponseEntity.status(ex.getHttpStatus()).body(response);
  }

  @ExceptionHandler(AccountLockedException.class)
  @ResponseStatus(HttpStatus.LOCKED)
  public ResponseEntity<ApiResponse<Object>> handleAccountLockedException(
      AccountLockedException ex) {

    log.warn("Account locked: {}", ex.getMessage());

    ApiResponse<Object> response = ApiResponse.error(
        "ACCOUNT_LOCKED"
    );
    return ResponseEntity.status(HttpStatus.LOCKED).body(response);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {

    log.error("Unexpected error occurred", ex);

    ApiResponse<Object> response = ApiResponse.error(
        "UNEXPECTED_ERR"
    );
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
}
