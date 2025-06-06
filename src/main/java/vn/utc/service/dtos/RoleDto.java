package vn.utc.service.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vn.utc.service.entity.Role;

import java.io.Serializable;
import java.time.Instant;

/** DTO for {@link Role} */
public record RoleDto(
    Integer id,
    @NotNull @Size(max = 50) String name,
    @Size(max = 255) String description,
    Instant createdAt,
    Instant updatedAt)
    implements Serializable {}
