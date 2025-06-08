package vn.utc.service.dtos;

import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/** DTO for {@link vn.utc.service.entity.StaffProfile} */
public record StaffProfileDto(
    Integer userId,
    @Size(max = 50) String username,
    @Size(max = 100) String email,
    @Size(max = 20) String phone,
    Instant createdAt,
    Instant lastLogin,
    Boolean isActive,
    @Size(max = 50) String firstName,
    @Size(max = 50) String lastName,
    LocalDate hireDate,
    BigDecimal hourlyRate,
    @Size(max = 50) String position,
    @Size(max = 100) String specialization)
    implements Serializable {}
