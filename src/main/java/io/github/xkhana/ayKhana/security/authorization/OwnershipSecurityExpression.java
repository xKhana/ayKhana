package io.github.xkhana.ayKhana.security.authorization;

import io.github.xkhana.ayKhana.entity.OwnableEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

@RequiredArgsConstructor
public class OwnershipSecurityExpression<T extends JpaRepository<? extends OwnableEntity, Long>> {
  private final T repo;

  public boolean isOwner(Long id, String username) {
    return repo.findById(id)
        .map(entity ->
          entity.getUser() != null && entity.getUser().getUsername().equals(username)
        )
        .orElse(false);
  }
}
