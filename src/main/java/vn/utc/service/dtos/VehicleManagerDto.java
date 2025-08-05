package vn.utc.service.dtos;

import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

public record VehicleManagerDto(
    @NotNull(message = "Customer ID is required")
    Integer customerId,
    Integer vehicleId,
    @NotNull(message = "Make is required")
        @Size(max = 50, message = "Make must not exceed 50 characters")
        String make,
    @NotNull(message = "Model is required")
        @Size(max = 50, message = "Model must not exceed 50 characters")
        String model,
    @NotNull(message = "Year is required")
        @Min(value = 1900, message = "Year must be at least 1900")
        @Max(value = 2030, message = "Year must not exceed 2030")
        Integer year,
    @NotNull(message = "License plate is required")
        @Size(max = 20, message = "License plate must not exceed 20 characters")
        String licensePlate,
    @Size(max = 17, message = "VIN must not exceed 17 characters")
        @Pattern(
            regexp = "^[A-HJ-NPR-Z0-9]{17}$",
            message = "VIN must be exactly 17 alphanumeric characters (excluding I, O, Q)")
        String vin,
    @Size(max = 30, message = "Color must not exceed 30 characters") String color,
    @Min(value = 0, message = "Mileage must be non-negative") Integer mileage,
    LocalDate lastServiceDate,
    Instant registrationDate)
    implements Serializable {}
