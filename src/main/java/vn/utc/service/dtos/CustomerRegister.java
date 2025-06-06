package vn.utc.service.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class CustomerRegister implements Serializable {
    private Integer id;
    @NotNull
    @Size(max = 50)
    private String firstName;
    @NotNull @Size(max = 50)
    private String lastName;
    @Size(max = 255)
    private String address;
    @Size(max = 50)
    private String city;
    @Size(max = 50)
    private String state;
    @Size(max = 20)
    private String zipCode;
    @Size(max = 20)
    private String preferredContactMethod;
    private String notes;
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
