package vn.utc.service.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class CustomerRegister implements Serializable {
    private Integer id;
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must not exceed 50 characters")
    private String firstName;
    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    private String lastName;
    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;
    @Size(max = 50, message = "City must not exceed 50 characters")
    private String city;
    @Size(max = 50, message = "State must not exceed 50 characters")
    private String state;
    @Size(max = 20, message = "Zip code must not exceed 20 characters")
    @Pattern(regexp = "^[0-9A-Za-z\\s\\-]+$", message = "Zip code should contain only letters, digits, spaces, and hyphens")
    private String zipCode;
    @Size(max = 20, message = "Preferred contact method must not exceed 20 characters")
    private String preferredContactMethod;
    private String notes;
    @Valid
    @NotNull(message = "User information is required")
    private UserDto user;

    public CustomerRegister setId(Integer id) {
        this.id = id;
        return this;
    }

    public CustomerRegister setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public CustomerRegister setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public CustomerRegister setAddress(String address) {
        this.address = address;
        return this;
    }

    public CustomerRegister setCity(String city) {
        this.city = city;
        return this;
    }

    public CustomerRegister setState(String state) {
        this.state = state;
        return this;
    }

    public CustomerRegister setZipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public CustomerRegister setPreferredContactMethod(String preferredContactMethod) {
        this.preferredContactMethod = preferredContactMethod;
        return this;
    }

    public CustomerRegister setNotes(String notes) {
        this.notes = notes;
        return this;
    }

    public CustomerRegister setUser(UserDto user) {
        this.user = user;
        return this;
    }
}
