package vn.utc.service.dtos;

import java.io.Serializable;

/**
 * DTO for maintenance schedule items
 */
public record MaintenanceItemDto(
    Integer id,
    String description,
    String dueDate,
    Integer mileage,
    String status,
    String priority,
    String notes
) implements Serializable {} 