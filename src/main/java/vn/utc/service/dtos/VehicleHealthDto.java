package vn.utc.service.dtos;

import java.io.Serializable;
import java.util.List;

import vn.utc.service.dtos.ComponentHealthDto;
import vn.utc.service.dtos.ComponentStatusDto;
import vn.utc.service.dtos.HealthHistoryDto;

/**
 * DTO for vehicle health data
 */
public record VehicleHealthDto(
    ComponentHealthDto engine,
    ComponentHealthDto transmission,
    ComponentHealthDto brakes,
    ComponentHealthDto suspension,
    ComponentHealthDto electrical,
    ComponentStatusDto componentsStatus,
    String trending,
    Integer changeSinceLastService,
    List<HealthHistoryDto> historyData
) implements Serializable {}

/**
 * DTO for individual component health
 */
//public record ComponentHealthDto(
//    Integer score,
//    String notes
//) implements Serializable {}
//
///**
// * DTO for component status summary
// */
//public record ComponentStatusDto(
//    Integer good,
//    Integer fair,
//    Integer poor
//) implements Serializable {}
//
///**
// * DTO for health history data points
// */
//public record HealthHistoryDto(
//    String date,
//    Integer score
//) implements Serializable {}