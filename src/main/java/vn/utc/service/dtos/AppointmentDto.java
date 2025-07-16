package vn.utc.service.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import vn.utc.service.entity.Appointment;

import java.io.Serializable;
import java.time.Instant;

/** DTO for {@link Appointment} */
public record AppointmentDto(
    Integer appointmentId,
    
    @NotNull(message = "Appointment date is required")
    @Future(message = "Appointment date must be in the future")
    Instant appointmentDate,
    
    @Future(message = "Estimated completion date must be in the future")
    Instant estimatedCompletion,
    
    @Size(max = 20, message = "Status must not exceed 20 characters")
    @Pattern(regexp = "^(PENDING|SCHEDULED|IN_PROGRESS|COMPLETED|CANCELLED|NO_SHOW)$",
             message = "Status must be one of: SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED, NO_SHOW")
    String status,
    
    @NotNull(message = "Service type is required")
    @Size(max = 100, message = "Service type must not exceed 100 characters")
    String serviceType,
    
    String description,
    
    VehicleDto vehicle,
    Instant createdAt,
    Instant updatedAt,
    // Receptionist-specific fields
    Integer customerId,
    CustomerRegister customerRegister
) implements Serializable {}
