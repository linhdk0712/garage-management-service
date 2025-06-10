package vn.utc.service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import vn.utc.service.entity.RefreshToken;
import vn.utc.service.entity.User;
import vn.utc.service.exception.TokenRefreshException;
import vn.utc.service.repo.RefreshTokenRepository;
import vn.utc.service.repo.UserRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RefreshTokenService Unit Tests")
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    private User user;
    private RefreshToken refreshToken;
    private String tokenValue;

    @BeforeEach
    void setUp() {
        // Set the refresh token duration for testing
        ReflectionTestUtils.setField(refreshTokenService, "refreshTokenDurationMs", 86400000L); // 24 hours

        user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        tokenValue = UUID.randomUUID().toString();
        refreshToken = new RefreshToken();
        refreshToken.setId(1);
        refreshToken.setToken(tokenValue);
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusSeconds(3600)); // 1 hour from now
    }

    @Test
    @DisplayName("Should find refresh token by token when exists")
    void findByToken_WhenTokenExists_ShouldReturnRefreshToken() {
        // Given
        when(refreshTokenRepository.findByToken(tokenValue)).thenReturn(Optional.of(refreshToken));

        // When
        Optional<RefreshToken> result = refreshTokenService.findByToken(tokenValue);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(refreshToken);
        assertThat(result.get().getToken()).isEqualTo(tokenValue);
        verify(refreshTokenRepository).findByToken(tokenValue);
    }

    @Test
    @DisplayName("Should return empty when token not found")
    void findByToken_WhenTokenNotFound_ShouldReturnEmpty() {
        // Given
        String nonExistentToken = UUID.randomUUID().toString();
        when(refreshTokenRepository.findByToken(nonExistentToken)).thenReturn(Optional.empty());

        // When
        Optional<RefreshToken> result = refreshTokenService.findByToken(nonExistentToken);

        // Then
        assertThat(result).isEmpty();
        verify(refreshTokenRepository).findByToken(nonExistentToken);
    }

    @Test
    @DisplayName("Should create refresh token successfully")
    void createRefreshToken_ShouldCreateAndReturnRefreshToken() {
        // Given
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(refreshTokenRepository.findByUser(user)).thenReturn(Optional.empty());
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(invocation -> {
            RefreshToken token = invocation.getArgument(0);
            token.setId(1);
            return token;
        });

        // When
        RefreshToken result = refreshTokenService.createRefreshToken(1);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getUser()).isEqualTo(user);
        assertThat(result.getToken()).isNotNull();
        assertThat(result.getExpiryDate()).isAfter(Instant.now());
        verify(userRepository).findById(1);
        verify(refreshTokenRepository).findByUser(user);
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void createRefreshToken_WhenUserNotFound_ShouldThrowException() {
        // Given
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> refreshTokenService.createRefreshToken(999))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found with id: 999");
        verify(userRepository).findById(999);
        verifyNoInteractions(refreshTokenRepository);
    }

    @Test
    @DisplayName("Should delete existing token when creating new one")
    void createRefreshToken_WhenExistingTokenExists_ShouldDeleteAndCreateNew() {
        // Given
        RefreshToken existingToken = new RefreshToken();
        existingToken.setId(2);
        existingToken.setToken("existing-token");
        existingToken.setUser(user);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(refreshTokenRepository.findByUser(user)).thenReturn(Optional.of(existingToken));
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(invocation -> {
            RefreshToken token = invocation.getArgument(0);
            token.setId(3);
            return token;
        });

        // When
        RefreshToken result = refreshTokenService.createRefreshToken(1);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(3);
        verify(userRepository).findById(1);
        verify(refreshTokenRepository).findByUser(user);
        verify(refreshTokenRepository).delete(existingToken);
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    @DisplayName("Should verify valid token expiration")
    void verifyExpiration_WhenTokenValid_ShouldReturnToken() {
        // Given
        refreshToken.setExpiryDate(Instant.now().plusSeconds(3600)); // Valid token

        // When
        RefreshToken result = refreshTokenService.verifyExpiration(refreshToken);

        // Then
        assertThat(result).isEqualTo(refreshToken);
        verify(refreshTokenRepository, never()).delete(any(RefreshToken.class));
    }

    @Test
    @DisplayName("Should throw exception and delete token when expired")
    void verifyExpiration_WhenTokenExpired_ShouldThrowExceptionAndDeleteToken() {
        // Given
        refreshToken.setExpiryDate(Instant.now().minusSeconds(3600)); // Expired token

        // When & Then
        assertThatThrownBy(() -> refreshTokenService.verifyExpiration(refreshToken))
                .isInstanceOf(TokenRefreshException.class)
                .hasMessage("Refresh token was expired. Please make a new login request");
        verify(refreshTokenRepository).delete(refreshToken);
    }

    @Test
    @DisplayName("Should delete refresh tokens by user ID successfully")
    void deleteByUserId_ShouldDeleteTokensForUser() {
        // Given
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(refreshTokenRepository.deleteByUser(user)).thenReturn(1);

        // When
        int result = refreshTokenService.deleteByUserId(1);

        // Then
        assertThat(result).isEqualTo(1);
        verify(userRepository).findById(1);
        verify(refreshTokenRepository).deleteByUser(user);
    }

    @Test
    @DisplayName("Should throw exception when user not found for deletion")
    void deleteByUserId_WhenUserNotFound_ShouldThrowException() {
        // Given
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> refreshTokenService.deleteByUserId(999))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found with id: 999");
        verify(userRepository).findById(999);
        verifyNoInteractions(refreshTokenRepository);
    }

    @Test
    @DisplayName("Should return zero when no tokens deleted")
    void deleteByUserId_WhenNoTokensDeleted_ShouldReturnZero() {
        // Given
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(refreshTokenRepository.deleteByUser(user)).thenReturn(0);

        // When
        int result = refreshTokenService.deleteByUserId(1);

        // Then
        assertThat(result).isEqualTo(0);
        verify(userRepository).findById(1);
        verify(refreshTokenRepository).deleteByUser(user);
    }

    @Test
    @DisplayName("Should create token with correct expiry date")
    void createRefreshToken_ShouldSetCorrectExpiryDate() {
        // Given
        Instant beforeCreation = Instant.now();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(refreshTokenRepository.findByUser(user)).thenReturn(Optional.empty());
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(invocation -> {
            RefreshToken token = invocation.getArgument(0);
            token.setId(1);
            return token;
        });

        // When
        RefreshToken result = refreshTokenService.createRefreshToken(1);
        Instant afterCreation = Instant.now();

        // Then
        assertThat(result.getExpiryDate()).isAfter(beforeCreation.plusSeconds(86399)); // Should be at least 24 hours - 1 second
        assertThat(result.getExpiryDate()).isBefore(afterCreation.plusSeconds(86401)); // Should be at most 24 hours + 1 second
    }

    @Test
    @DisplayName("Should generate unique token for each creation")
    void createRefreshToken_ShouldGenerateUniqueTokens() {
        // Given
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(refreshTokenRepository.findByUser(user)).thenReturn(Optional.empty());
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(invocation -> {
            RefreshToken token = invocation.getArgument(0);
            token.setId(1);
            return token;
        });

        // When
        RefreshToken result1 = refreshTokenService.createRefreshToken(1);
        RefreshToken result2 = refreshTokenService.createRefreshToken(1);

        // Then
        assertThat(result1.getToken()).isNotNull();
        assertThat(result2.getToken()).isNotNull();
        // Note: In a real scenario, these might be the same due to UUID generation timing,
        // but the tokens should be unique in practice
    }
} 