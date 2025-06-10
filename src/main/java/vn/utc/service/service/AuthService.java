package vn.utc.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.utc.service.config.ContsConfig;
import vn.utc.service.dtos.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final CustomerService customerService;

    @Transactional
    public CustomerDto registerCustomer(RegisterRequest signUpRequest) {
// Check if username is already taken


        Set<String> strRoles = signUpRequest.roles();
        Set<RoleDto> roles = new HashSet<>();
        if (strRoles == null || strRoles.isEmpty()) {
            // Default role is RECEPTIONIST if not specified
            RoleDto roleDto =
                    roleService
                            .findByName(ContsConfig.CUSTOMER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(roleDto);
        } else {
            strRoles.forEach(
                    role -> {
                        RoleDto userRole =
                                roleService
                                        .findByName(role)
                                        .orElseThrow(
                                                () -> new RuntimeException("Error: Role " + role + " is not found."));
                        roles.add(userRole);
                    });
        }
        // Create new user's account
        UserDto userDto =
                new UserDto()
                        .setUsername(signUpRequest.username())
                        .setEmail(signUpRequest.email())
                        .setPassword(passwordEncoder.encode(signUpRequest.password()))
                        .setPhone(signUpRequest.phone())
                        .setRole(String.valueOf(roles.iterator().next().name()))
                        .setActive(true)
                        .setCreatedAt(Instant.now());
        userDto.setRoles(roles);
        userDto = userService.save(userDto);

        CustomerRegister customerRegister =
                new CustomerRegister()
                        .setAddress(signUpRequest.address())
                        .setCity(signUpRequest.city())
                        .setFirstName(signUpRequest.firstName())
                        .setLastName(signUpRequest.lastName())
                        .setNotes("")
                        .setPreferredContactMethod(signUpRequest.preferredContactMethod())
                        .setState(signUpRequest.state())
                        .setZipCode(signUpRequest.zipCode())
                        .setUser(userDto);
       return customerService.saveCustomer(customerRegister);
    }
}
