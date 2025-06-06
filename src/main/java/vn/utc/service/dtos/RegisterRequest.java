package vn.utc.service.dtos;

import java.io.Serializable;
import java.util.Set;

public record RegisterRequest(
    String username,
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
    Set<String> roles)
    implements Serializable {}
