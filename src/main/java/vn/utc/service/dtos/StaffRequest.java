package vn.utc.service.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public record StaffRequest(
    @NotBlank(message = "Username is required")
    @Size(max = 50, message = "Username must not exceed 50 characters")
    String username,
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    String email,
    
    @Size(max = 20, message = "Phone must not exceed 20 characters")
    @Pattern(regexp = "^[+]?[0-9\\s\\-\\(\\)]+$", message = "Phone number should contain only digits, spaces, hyphens, and parentheses")
    String phone,
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
    String password,
    
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must not exceed 50 characters")
    String firstName,
    
    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    String lastName,
    
    @Size(max = 255, message = "Address must not exceed 255 characters")
    String address,
    
    @Size(max = 50, message = "City must not exceed 50 characters")
    String city,
    
    @Size(max = 50, message = "State must not exceed 50 characters")
    String state,
    
    @Size(max = 20, message = "Zip code must not exceed 20 characters")
    @Pattern(regexp = "^[0-9A-Za-z\\s\\-]+$", message = "Zip code should contain only letters, digits, spaces, and hyphens")
    String zipCode,
    
    @Size(max = 20, message = "Preferred contact method must not exceed 20 characters")
    String preferredContactMethod,
    
    @NotBlank(message = "Position is required")
    @Size(max = 50, message = "Position must not exceed 50 characters")
    String position,
    
    @Size(max = 100, message = "Specialization must not exceed 100 characters")
    String specialization,
    
    @NotNull(message = "Hire date is required")
    @PastOrPresent(message = "Hire date must be in the past or present")
    LocalDate hireDate,
    
    @NotNull(message = "Hourly rate is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Hourly rate must be greater than 0")
    BigDecimal hourlyRate
) implements Serializable {}
