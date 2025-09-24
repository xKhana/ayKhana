package io.github.xkhana.ayKhana.model.response;


import lombok.Data;

@Data
public class CustomerRegistrationResponse {
  private String firstName;
  private String lastName;
  private String username;
  private String email;
  private String phone;
}
