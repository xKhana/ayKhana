package io.github.xkhana.ayKhana.controller;

import io.github.xkhana.ayKhana.model.request.AddAddressRequest;
import io.github.xkhana.ayKhana.model.request.UpdateAddressRequest;
import io.github.xkhana.ayKhana.model.response.AddressResponse;

import java.util.List;

public interface UserService {

  AddressResponse updateAddress(Long addressId, UpdateAddressRequest updateAddressRequest);

  AddressResponse addAddressByUsername(AddAddressRequest addAddressRequest, String username);

  void deleteAddress(Long addressId);

  List<AddressResponse> getAddressesByUsername(String username);

  AddressResponse getAddress(Long addressId);
}
