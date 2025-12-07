package io.github.xkhana.ayKhana.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Entity
@Data
@Table(name = "orders")
public class Order implements OwnableEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @CreatedDate
  private Instant createdDate;

  @Column(nullable = false)
  private Long totalPrice;

  @ManyToOne
  @JoinColumn(name = "address_id", referencedColumnName = "id")
  private Address address;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private User user;

  @Enumerated(EnumType.STRING)
  @JdbcType(PostgreSQLEnumJdbcType.class)
  private PaymentMethod paymentMethod;

  @Enumerated(EnumType.STRING)
  @JdbcType(PostgreSQLEnumJdbcType.class)
  private Status status;

  private String note;

  public enum PaymentMethod {CASH, CARD, STRIPE}

  public enum Status {ON_DELIVERY, SHIPPED, CANCELLED}
}
