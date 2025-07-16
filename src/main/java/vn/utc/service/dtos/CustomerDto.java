package vn.utc.service.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/** DTO for {@link vn.utc.service.entity.Customer} */
public record CustomerDto(
    Integer id,
    UserDto user,
    @NotNull @Size(max = 50) String firstName,
    @NotNull @Size(max = 50) String lastName,
    @Size(max = 255) String address,
    @Size(max = 50) String city,
    @Size(max = 50) String state,
    @Size(max = 20) String zipCode,
    @Size(max = 20) String preferredContactMethod,
    String notes)
    implements Serializable {}
