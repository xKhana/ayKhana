package io.github.xkhana.ayKhana.service;

import io.github.xkhana.ayKhana.controller.UserService;
import io.github.xkhana.ayKhana.entity.Address;
import io.github.xkhana.ayKhana.exception.BusinessException;
import io.github.xkhana.ayKhana.model.request.AddAddressRequest;
import io.github.xkhana.ayKhana.model.request.UpdateAddressRequest;
import io.github.xkhana.ayKhana.model.response.AddressResponse;
import io.github.xkhana.ayKhana.repository.AddressRepository;
import io.github.xkhana.ayKhana.repository.UserRepository;
import io.github.xkhana.ayKhana.util.ModelMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final AddressRepository addressRepository;
  private final ModelMapperUtil modelMapper;

  @Override
  @Transactional
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  public AddressResponse addAddressByUsername(AddAddressRequest addAddressRequest, String username) {
    Address address = new Address();
    address.setUser(userRepository.findByUsername(username).orElseThrow(
        () -> new BusinessException("USER_NOT_FOUND", "User not found", HttpStatus.NOT_FOUND)));
    modelMapper.map(addAddressRequest, address);
    return modelMapper.map(addressRepository.save(address), AddressResponse.class);
  }

  @Override
  @Transactional
  @PreAuthorize("hasRole('ADMIN') || @addressSE.isOwner(#addressId, authentication.name)")
  public AddressResponse updateAddress(Long addressId, UpdateAddressRequest updateAddressRequest) {
    Address address = addressRepository.findById(addressId).orElseThrow(
        () -> new BusinessException("ADDRESS_NOT_FOUND", "Address not found", HttpStatus.NOT_FOUND));
    modelMapper.map(updateAddressRequest, address);
    return modelMapper.map(addressRepository.save(address), AddressResponse.class);
  }

  @Override
  @Transactional
  @PreAuthorize("hasRole('ADMIN') || @addressSE.isOwner(#addressId, authentication.name)")
  public void deleteAddress(Long addressId) {
    addressRepository.deleteById(addressId);
  }

  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  @Override
  public List<AddressResponse> getAddressesByUsername(String username) {
    return addressRepository.findAllByUser_Username(username)
        .stream()
        .map(el -> modelMapper.map(el, AddressResponse.class))
        .collect(Collectors.toList());
  }

  @PreAuthorize("hasRole('ADMIN') || @addressSE.isOwner(#addressId, authentication.name)")
  @Override
  public AddressResponse getAddress(Long addressId) {
    Address address = addressRepository.findById(addressId)
        .orElseThrow(() -> new BusinessException(
            "ADDRESS_NOT_FOUND",
            "Address not found",
            HttpStatus.NOT_FOUND));
    return modelMapper.map(address, AddressResponse.class);
  }
}
