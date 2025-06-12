package vn.utc.service.dtos;

import java.io.Serializable;

/**
 * DTO for component status summary
 */
public record ComponentStatusDto(
    Integer good,
    Integer fair,
    Integer poor
) implements Serializable {} 