package vn.utc.service.dtos;

import java.io.Serializable;
import java.util.List;

/** DTO for customer dashboard data including appointments and vehicles */
public record CustomerDashboardDto(
    List<AppointmentDto> appointments,
    List<VehicleDto> vehicles
) implements Serializable {} 