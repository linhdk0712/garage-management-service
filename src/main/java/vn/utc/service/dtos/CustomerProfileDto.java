package vn.utc.service.dtos;

import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.Instant;

/** DTO for {@link vn.utc.service.entity.CustomerProfile} */
public record CustomerProfileDto(
    Integer userId,
    @Size(max = 50) String username,
    @Size(max = 100) String email,
    @Size(max = 20) String phone,
    Instant createdAt,
    Instant lastLogin,
    Boolean isActive,
    @Size(max = 50) String firstName,
    @Size(max = 50) String lastName,
    @Size(max = 255) String address,
    @Size(max = 50) String city,
    @Size(max = 50) String state,
    @Size(max = 20) String zipCode,
    String notes)
    implements Serializable {}
