package vn.utc.service.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/** DTO for {@link vn.utc.service.entity.Staff} */
public record StaffDto(
    Integer id,
    @NotNull @Size(max = 50) String firstName,
    @NotNull @Size(max = 50) String lastName,
    @Size(max = 50) String position,
    @Size(max = 100) String specialization,
    LocalDate hireDate,
    BigDecimal hourlyRate)
    implements Serializable {}
