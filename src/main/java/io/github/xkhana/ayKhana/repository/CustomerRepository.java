package io.github.xkhana.ayKhana.repository;

import io.github.xkhana.ayKhana.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
  Optional<Customer> findByUsername(String name);
}
