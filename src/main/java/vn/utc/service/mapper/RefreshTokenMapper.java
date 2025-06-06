package vn.utc.service.mapper;

import org.mapstruct.*;
import vn.utc.service.entity.RefreshToken;
import vn.utc.service.entity.RefreshTokenDto;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface RefreshTokenMapper {
  RefreshToken toEntity(RefreshTokenDto refreshTokenDto);

  RefreshTokenDto toDto(RefreshToken refreshToken);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  RefreshToken partialUpdate(
      RefreshTokenDto refreshTokenDto, @MappingTarget RefreshToken refreshToken);
}
