package io.github.xkhana.ayKhana.model.response;

import io.github.xkhana.ayKhana.model.error.ErrorDetails;
import io.github.xkhana.ayKhana.model.error.ValidationError;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;


@Setter
@Getter
public class ApiResponse<T> {
  private boolean success;
  private T data;
  private ErrorDetails errors;
  private String timestamp;


  public ApiResponse() {
    this.timestamp = Instant.now().toString();
  }

  public ApiResponse(boolean success) {
    this();
    this.success = success;
  }

  public ApiResponse(boolean success, T data) {
    this(success);
    this.data = data;
  }


  public static <T> ApiResponse<T> success(T data) {
    return new ApiResponse<>(true, data);
  }

  public static <T> ApiResponse<T> success() {
    return new ApiResponse<>(true);
  }

  public static <T> ApiResponse<T> validationError(List<ValidationError> errors) {
    ApiResponse<T> response = new ApiResponse<>(false);
    response.setErrors(new ErrorDetails("VALIDATION_ERR", errors));
    return response;
  }

  public static <T> ApiResponse<T> error(ErrorDetails error) {
    ApiResponse<T> response = new ApiResponse<>(false);
    response.setErrors(error);
    return response;
  }

  public static <T> ApiResponse<T> error(String code) {
    return error(new ErrorDetails(code));
  }

  public static <T> ApiResponse<T> error() {
    return new ApiResponse<>(false);
  }
}