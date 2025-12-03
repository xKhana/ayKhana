package io.github.xkhana.ayKhana.controller;

import io.github.xkhana.ayKhana.model.request.AddAddressRequest;
import io.github.xkhana.ayKhana.model.request.UpdateAddressRequest;
import io.github.xkhana.ayKhana.model.response.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/account")
public class AccountController {

  private final UserService userService;

  @PostMapping("/addresses")
  public ResponseEntity<ApiResponse<?>> addAddress(@RequestBody @Valid AddAddressRequest addAddressRequest, Principal principal) {
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(userService.addAddressByUsername(addAddressRequest, principal.getName())));
  }

  @PatchMapping("/addresses/{addressId}")
  public ResponseEntity<ApiResponse<?>> updateAddress(@PathVariable @Positive Long addressId, @RequestBody @Valid UpdateAddressRequest updateAddressRequest) {
    return ResponseEntity.ok(ApiResponse.success(userService.updateAddress(addressId, updateAddressRequest)));
  }

  @DeleteMapping("/addresses/{addressId}")
  public ResponseEntity<ApiResponse<?>> deleteAddress(@PathVariable @Positive Long addressId) {
    userService.deleteAddress(addressId);
    return ResponseEntity.ok(ApiResponse.success());
  }

  @GetMapping("/addresses")
  public ResponseEntity<ApiResponse<?>> getAddresses(Principal principal) {
    return ResponseEntity.ok(ApiResponse.success(userService.getAddressesByUsername(principal.getName())));
  }

  @GetMapping("/addresses/{addressId}")
  public ResponseEntity<ApiResponse<?>> getAddress(@PathVariable @Positive Long addressId) {
    return ResponseEntity.ok(ApiResponse.success(userService.getAddress(addressId)));
  }


}
