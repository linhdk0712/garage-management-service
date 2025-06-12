package vn.utc.service.dtos;

import java.io.Serializable;

/**
 * DTO for health history data points
 */
public record HealthHistoryDto(
    String date,
    Integer score
) implements Serializable {} 