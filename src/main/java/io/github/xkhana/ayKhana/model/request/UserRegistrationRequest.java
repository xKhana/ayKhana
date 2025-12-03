package io.github.xkhana.ayKhana.model.request;

import io.github.xkhana.ayKhana.validation.password.Password;
import io.github.xkhana.ayKhana.validation.phone_number.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserRegistrationRequest {

  @NotBlank(message = "SHOULD_NOT_BLANK")
  private String firstName;

  @NotBlank(message = "SHOULD_NOT_BLANK")
  private String lastName;

  @NotBlank(message = "SHOULD_NOT_BLANK")
  @Pattern(regexp = "^[a-zA-Z0-9_-]{3,16}$", message = "INVALID_USERNAME")
  private String username;

  @NotBlank(message = "SHOULD_NOT_BLANK")
  @Email
  private String email;

  @NotBlank(message = "SHOULD_NOT_BLANK")
  @PhoneNumber
  private String phone;

  @NotBlank(message = "SHOULD_NOT_BLANK")
  @Password
  private String password;

  @NotBlank(message = "SHOULD_NOT_BLANK")
  private String passwordConfirmation;
}
