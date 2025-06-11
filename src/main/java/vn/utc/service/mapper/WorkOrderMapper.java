package vn.utc.service.mapper;

import org.mapstruct.*;
import vn.utc.service.entity.WorkOrder;
import vn.utc.service.dtos.WorkOrderDto;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface WorkOrderMapper {
  WorkOrder toEntity(WorkOrderDto workOrderDto);

  WorkOrderDto toDto(WorkOrder workOrder);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  WorkOrder partialUpdate(WorkOrderDto workOrderDto, @MappingTarget WorkOrder workOrder);
} 