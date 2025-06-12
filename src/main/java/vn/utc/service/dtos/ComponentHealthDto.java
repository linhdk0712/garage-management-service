package vn.utc.service.dtos;

import java.io.Serializable;

/**
 * DTO for individual component health
 */
public record ComponentHealthDto(
    Integer score,
    String notes
) implements Serializable {} 