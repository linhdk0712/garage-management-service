package vn.utc.service.mapper;

import org.mapstruct.*;
import vn.utc.service.entity.Role;
import vn.utc.service.dtos.RoleDto;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {
  Role toEntity(RoleDto roleDto);

  RoleDto toDto(Role role);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Role partialUpdate(RoleDto roleDto, @MappingTarget Role role);
}
