package vn.utc.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import vn.utc.service.entity.RefreshToken;
import vn.utc.service.entity.User;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
  Optional<RefreshToken> findByToken(String token);

  @Modifying
  int deleteByUser(User user);

  Optional<RefreshToken> findByUser(User user);
}
