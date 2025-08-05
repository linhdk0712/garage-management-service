package vn.utc.service.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Detailed DTO for WorkOrder with nested objects for manager view
 */
public record WorkOrderDetailDto(
    Integer workOrderId,
    Integer appointmentId,
    
    // Appointment details
    AppointmentSummaryDto appointment,
    
    // Customer details
    CustomerSummaryDto customer,
    
    // Vehicle details
    VehicleSummaryDto vehicle,
    
    // Staff details
    StaffSummaryDto assignedStaff,
    
    Instant startTime,
    Instant endTime,
    String status,
    String diagnosticNotes,
    BigDecimal totalCost,
    Instant createdAt,
    Instant updatedAt
) implements Serializable {
    
    public record AppointmentSummaryDto(
        Integer appointmentId,
        String appointmentDate,
        String serviceType,
        String status
    ) implements Serializable {}
    
    public record CustomerSummaryDto(
        Integer customerId,
        String firstName,
        String lastName,
        String email,
        String phone
    ) implements Serializable {}
    
    public record VehicleSummaryDto(
        Integer vehicleId,
        String make,
        String model,
        Integer year,
        String licensePlate,
        String vin,
        String color,
        Integer mileage
    ) implements Serializable {}
    
    public record StaffSummaryDto(
        Integer staffId,
        String firstName,
        String lastName,
        String position
    ) implements Serializable {}
} 