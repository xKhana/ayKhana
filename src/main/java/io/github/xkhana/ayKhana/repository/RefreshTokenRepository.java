package io.github.xkhana.ayKhana.repository;

import io.github.xkhana.ayKhana.entity.RefreshToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);

  @Modifying
  @Transactional
  @Query("UPDATE RefreshToken rt SET rt.token = :newToken WHERE rt.user.username = :username")
  int updateTokenByUsername(@Param("username") String username, @Param("newToken") String newToken);
}
