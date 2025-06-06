package vn.utc.service.dtos;

import java.io.Serializable;
import java.util.Set;

public record VehicleCustomerDto(
        UserDto userDto,
        Set<VehicleDto> vehicleDto
) implements Serializable {}
