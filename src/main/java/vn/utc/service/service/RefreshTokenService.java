package vn.utc.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.utc.service.entity.RefreshToken;
import vn.utc.service.entity.User;
import vn.utc.service.exception.TokenRefreshException;
import vn.utc.service.repo.RefreshTokenRepository;
import vn.utc.service.repo.UserRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
  @Value("${app.jwt.refresh-expiration}")
  private Long refreshTokenDurationMs;

  private final RefreshTokenRepository refreshTokenRepository;
  private final UserRepository userRepository;

  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  public RefreshToken createRefreshToken(Integer userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

    // Check if user already has a refresh token
    Optional<RefreshToken> existingToken = refreshTokenRepository.findByUser(user);
    if (existingToken.isPresent()) {
      refreshTokenRepository.delete(existingToken.get());
    }

    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setUser(user);
    refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
    refreshToken.setToken(UUID.randomUUID().toString());

    return refreshTokenRepository.save(refreshToken);
  }

  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new TokenRefreshException(
           "Refresh token was expired. Please make a new login request");
    }

    return token;
  }

  @Transactional
  public int deleteByUserId(Integer userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    return refreshTokenRepository.deleteByUser(user);
  }
}
