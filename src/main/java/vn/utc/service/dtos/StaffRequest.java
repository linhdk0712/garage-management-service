package vn.utc.service.dtos;

import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record StaffRequest(String username,
                           String email,
                           String phone,
                           String password,
                           String firstName,
                           String lastName,
                           String address,
                           String city,
                           String state,
                           String zipCode,
                           String preferredContactMethod,
                           @Size(max = 50) String position,
                           @Size(max = 100) String specialization,
                           LocalDate hireDate,
                           BigDecimal hourlyRate) implements Serializable {}
