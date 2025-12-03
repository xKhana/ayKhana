package io.github.xkhana.ayKhana.model.response;

import io.github.xkhana.ayKhana.entity.Address;
import lombok.Data;

@Data
public class AddressResponse {
  private Long id;

  private String name;

  private Address.Type type;

  private String country;

  private String governorate;

  private String city;

  private String details;

  private String zipCode;

  private String phone;

  private Double latitude;

  private Double longitude;
}
