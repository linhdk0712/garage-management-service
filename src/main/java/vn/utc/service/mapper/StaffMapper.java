package vn.utc.service.mapper;

import org.mapstruct.*;
import vn.utc.service.dtos.StaffDto;
import vn.utc.service.entity.Staff;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface StaffMapper {
  Staff toEntity(StaffDto staffDto);

  StaffDto toDto(Staff staff);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Staff partialUpdate(StaffDto staffDto, @MappingTarget Staff staff);
}
