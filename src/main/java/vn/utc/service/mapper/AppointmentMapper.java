package vn.utc.service.mapper;

import org.mapstruct.*;
import vn.utc.service.entity.Appointment;
import vn.utc.service.dtos.AppointmentDto;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING,
    uses = {VehicleMapper.class})
public interface AppointmentMapper {
  @Mapping(target = "id", source = "appointmentId")
  Appointment toEntity(AppointmentDto appointmentDto);

  @Mapping(target = "vehicle", source = "vehicle")
  @Mapping(target = "appointmentId", source = "id")
  AppointmentDto toDto(Appointment appointment);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Appointment partialUpdate(AppointmentDto appointmentDto, @MappingTarget Appointment appointment);
}
