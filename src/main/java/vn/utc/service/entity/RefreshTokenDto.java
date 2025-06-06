package vn.utc.service.entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.Instant;

/** DTO for {@link RefreshToken} */
public record RefreshTokenDto(
    Integer id,
    @NotNull @Size(max = 255) String token,
    @NotNull Instant expiryDate,
    Instant createdAt)
    implements Serializable {}
