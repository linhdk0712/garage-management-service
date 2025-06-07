package vn.utc.service.mapper;

import org.mapstruct.*;
import vn.utc.service.dtos.CustomerProfileDto;
import vn.utc.service.entity.CustomerProfile;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerProfileMapper {
  CustomerProfile toEntity(CustomerProfileDto customerProfileDto);

  CustomerProfileDto toDto(CustomerProfile customerProfile);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  CustomerProfile partialUpdate(
      CustomerProfileDto customerProfileDto, @MappingTarget CustomerProfile customerProfile);
}
