package io.github.xkhana.ayKhana.model.request;

import io.github.xkhana.ayKhana.validation.password.Password;
import io.github.xkhana.ayKhana.validation.phone_number.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CustomerRegistrationRequest {

  @NotBlank
  private String firstName;

  @NotBlank
  private String lastName;

  @NotBlank
  @Pattern(regexp = "^[a-zA-Z0-9_-]{3,16}$")
  private String username;

  @NotBlank
  @Email
  private String email;

  @NotBlank
  @PhoneNumber
  private String phone;

  @NotBlank
  @Password
  private String password;

  @NotBlank
  private String passwordConfirmation;
}
