package vn.utc.service.mapper;

import org.mapstruct.*;
import vn.utc.service.dtos.CustomerDto;
import vn.utc.service.dtos.CustomerRegister;
import vn.utc.service.entity.Customer;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {
  Customer toEntity(CustomerDto customerDto);

  Customer toEntityFromRegister(CustomerRegister customerRegister);

  CustomerDto toDto(Customer customer);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Customer partialUpdate(CustomerDto customerDto, @MappingTarget Customer customer);
}
