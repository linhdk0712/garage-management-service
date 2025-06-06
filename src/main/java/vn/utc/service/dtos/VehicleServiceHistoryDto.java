package vn.utc.service.dtos;

import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/** DTO for {@link vn.utc.service.entity.VehicleServiceHistory} */
public record VehicleServiceHistoryDto(
    Integer vehicleId,
    @Size(max = 50) String make,
    @Size(max = 50) String model,
    Integer year,
    @Size(max = 20) String licensePlate,
    Integer appointmentId,
    Instant appointmentDate,
    @Size(max = 100) String serviceType,
    Integer workOrderId,
    Instant startTime,
    Instant endTime,
    @Size(max = 20) String workOrderStatus,
    BigDecimal totalCost,
    @Size(max = 100) String partName,
    Integer partQuantity,
    BigDecimal partTotalPrice,
    BigDecimal hoursWorked,
    BigDecimal laborCharge,
    Integer rating,
    String comments)
    implements Serializable {}
