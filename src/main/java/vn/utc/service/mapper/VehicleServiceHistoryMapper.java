package vn.utc.service.mapper;

import org.mapstruct.*;
import vn.utc.service.dtos.VehicleServiceHistoryDto;
import vn.utc.service.entity.VehicleServiceHistory;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface VehicleServiceHistoryMapper {
  VehicleServiceHistory toEntity(VehicleServiceHistoryDto vehicleServiceHistoryDto);

  VehicleServiceHistoryDto toDto(VehicleServiceHistory vehicleServiceHistory);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  VehicleServiceHistory partialUpdate(
      VehicleServiceHistoryDto vehicleServiceHistoryDto,
      @MappingTarget VehicleServiceHistory vehicleServiceHistory);
}
