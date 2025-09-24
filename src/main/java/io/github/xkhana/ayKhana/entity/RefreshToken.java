package io.github.xkhana.ayKhana.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Entity
@Data
@Table(name = "refresh_tokens")
@NoArgsConstructor
public class RefreshToken {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(unique = true, length = 1024)
  private String token;

  @OneToOne()
  @JoinColumn(name = "customer_id", referencedColumnName = "id")
  private Customer customer;

  public RefreshToken(Customer customer, String token) {
    this.customer = customer;
    this.token = token;
  }

}
