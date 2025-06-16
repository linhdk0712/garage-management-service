package vn.utc.service.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import vn.utc.service.entity.WorkOrder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/** DTO for {@link WorkOrder} */
public record WorkOrderDto(
    Integer id,
    
    @NotNull(message = "Appointment ID is required")
    Integer appointmentId,
    
    @NotNull(message = "Staff ID is required")
    Integer staffId,
    
    @NotNull(message = "Start time is required")
    Instant startTime,
    
    Instant endTime,
    
    @Size(max = 20, message = "Status must not exceed 20 characters")
    @Pattern(regexp = "^(PENDING|IN_PROGRESS|COMPLETED|CANCELLED)$", 
             message = "Status must be one of: PENDING, IN_PROGRESS, COMPLETED, CANCELLED")
    String status,
    
    String diagnosticNotes,
    
    @DecimalMin(value = "0.0", inclusive = true, message = "Total cost must be non-negative")
    BigDecimal totalCost,
    
    Instant createdAt,
    Instant updatedAt)
    implements Serializable {} 