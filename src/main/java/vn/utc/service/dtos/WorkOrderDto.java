package vn.utc.service.dtos;

import jakarta.validation.constraints.Size;
import vn.utc.service.entity.WorkOrder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/** DTO for {@link WorkOrder} */
public record WorkOrderDto(
    Integer id,
    Integer appointmentId,
    Integer staffId,
    Instant startTime,
    Instant endTime,
    @Size(max = 20) String status,
    String diagnosticNotes,
    BigDecimal totalCost,
    Instant createdAt,
    Instant updatedAt)
    implements Serializable {} 