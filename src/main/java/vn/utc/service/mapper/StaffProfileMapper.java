package vn.utc.service.mapper;

import org.mapstruct.*;
import vn.utc.service.dtos.StaffProfileDto;
import vn.utc.service.entity.StaffProfile;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface StaffProfileMapper {
  StaffProfile toEntity(StaffProfileDto staffProfileDto);

  StaffProfileDto toDto(StaffProfile staffProfile);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  StaffProfile partialUpdate(
      StaffProfileDto staffProfileDto, @MappingTarget StaffProfile staffProfile);
}
