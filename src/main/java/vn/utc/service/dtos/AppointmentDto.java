package vn.utc.service.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vn.utc.service.entity.Appointment;

import java.io.Serializable;
import java.time.Instant;

/** DTO for {@link Appointment} */
public record AppointmentDto(
    Integer id,
    @NotNull Instant appointmentDate,
    Instant estimatedCompletion,
    @Size(max = 20) String status,
    @NotNull @Size(max = 100) String serviceType,
    String description,
    Instant createdAt,
    Instant updatedAt)
    implements Serializable {}
