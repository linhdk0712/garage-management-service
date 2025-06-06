package vn.utc.service.mapper;

import org.mapstruct.*;
import vn.utc.service.entity.Appointment;
import vn.utc.service.dtos.AppointmentDto;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface AppointmentMapper {
  Appointment toEntity(AppointmentDto appointmentDto);

  AppointmentDto toDto(Appointment appointment);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Appointment partialUpdate(AppointmentDto appointmentDto, @MappingTarget Appointment appointment);
}
