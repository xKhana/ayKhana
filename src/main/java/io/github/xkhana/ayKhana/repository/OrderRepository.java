package io.github.xkhana.ayKhana.repository;

import io.github.xkhana.ayKhana.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
