package vn.utc.service.mapper;

import org.mapstruct.*;
import vn.utc.service.dtos.UserDto;
import vn.utc.service.entity.User;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
  User toEntity(UserDto userDto);

  UserDto toDto(User user);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  User partialUpdate(UserDto userDto, @MappingTarget User user);
}
