package io.github.xkhana.ayKhana.repository;

import io.github.xkhana.ayKhana.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
  List<Address> findAllByUser_Username(String username);
}
