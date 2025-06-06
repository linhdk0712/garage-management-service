package vn.utc.service.mapper;

import org.mapstruct.*;
import vn.utc.service.dtos.VehicleDto;
import vn.utc.service.entity.Vehicle;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface VehicleMapper {
  @Mapping(target = "id", source = "vehicleId")
  Vehicle toEntity(VehicleDto vehicleDto);

  @Mapping(target = "vehicleId", source = "id")
  VehicleDto toDto(Vehicle vehicle);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Vehicle partialUpdate(VehicleDto vehicleDto, @MappingTarget Vehicle vehicle);
}
