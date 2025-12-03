package io.github.xkhana.ayKhana.model.request;

import io.github.xkhana.ayKhana.entity.Address;
import io.github.xkhana.ayKhana.validation.phone_number.PhoneNumber;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddAddressRequest {
  @NotBlank(message = "SHOULD_NOT_BLANK")
  private String name;

  @NotNull(message = "SHOULD_NOT_BLANK")
  private Address.Type type;

  @NotBlank(message = "SHOULD_NOT_BLANK")
  private String country;

  @NotBlank(message = "SHOULD_NOT_BLANK")
  private String governorate;

  @NotBlank(message = "SHOULD_NOT_BLANK")
  private String city;

  @NotBlank(message = "SHOULD_NOT_BLANK")
  private String details;

  @NotBlank(message = "SHOULD_NOT_BLANK")
  private String zipCode;

  @PhoneNumber
  @NotBlank(message = "SHOULD_NOT_BLANK")
  private String phone;

  @NotNull(message = "SHOULD_NOT_BLANK")
  @DecimalMin(value = "-90.0", message = "OUT_OF_RANGE")
  @DecimalMax(value = "90.0", message = "OUT_OF_RANGE")
  private Double latitude;

  @NotNull(message = "SHOULD_NOT_BLANK")
  @DecimalMin(value = "-180.0", message = "OUT_OF_RANGE")
  @DecimalMax(value = "180.0", message = "OUT_OF_RANGE")
  private Double longitude;
}
