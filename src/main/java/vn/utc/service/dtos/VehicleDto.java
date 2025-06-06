package vn.utc.service.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

/** DTO for {@link vn.utc.service.entity.Vehicle} */
public record VehicleDto(
    Integer vehicleId,
    @NotNull @Size(max = 50) String make,
    @NotNull @Size(max = 50) String model,
    @NotNull Integer year,
    @NotNull @Size(max = 20) String licensePlate,
    @Size(max = 17) String vin,
    @Size(max = 30) String color,
    Integer mileage,
    LocalDate lastServiceDate,
    Instant registrationDate)
    implements Serializable {}
