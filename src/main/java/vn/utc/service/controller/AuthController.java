package vn.utc.service.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import vn.utc.service.config.JwtTokenProvider;
import vn.utc.service.dtos.*;
import vn.utc.service.entity.RefreshToken;
import vn.utc.service.exception.TokenRefreshException;
import vn.utc.service.service.CustomerService;
import vn.utc.service.service.RefreshTokenService;
import vn.utc.service.service.RoleService;
import vn.utc.service.service.UserService;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authenticate customer,user")
public class AuthController {
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider tokenProvider;
  private final RefreshTokenService refreshTokenService;
  private final UserService userService;
  private final RoleService roleService;
  private final CustomerService customerService;

  @PostMapping(
      value = "/login",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseDataDto> login(@Valid @RequestBody LoginDto loginDto) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = tokenProvider.generateToken(authentication);

    UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
    List<String> roles =
        userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    // Create refresh token
    RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
    JwtResponse jwtResponse =
        new JwtResponse()
            .setToken(jwt)
            .setRefreshToken(refreshToken.getToken())
            .setId(Long.valueOf(userDetails.getId()))
            .setUsername(userDetails.getUsername())
            .setEmail(userDetails.getEmail())
            .setRoles(roles);
    ResponseDataDto responseDataDto = new ResponseDataDto();
    responseDataDto.setData(jwtResponse);
    return ResponseEntity.ok(responseDataDto);
  }

  @PostMapping(
      value = "/refresh-token",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TokenRefreshResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
    String requestRefreshToken = request.getRefreshToken();

    return refreshTokenService
        .findByToken(requestRefreshToken)
        .map(refreshTokenService::verifyExpiration)
        .map(RefreshToken::getUser)
        .map(
            user -> {
              // Create new authentication token
              UserPrincipal userPrincipal = UserPrincipal.create(user);
              Authentication authentication =
                  new UsernamePasswordAuthenticationToken(
                      userPrincipal, null, userPrincipal.getAuthorities());

              String token = tokenProvider.generateToken(authentication);
              return ResponseEntity.ok(
                  new TokenRefreshResponse()
                      .setAccessToken(token)
                      .setRefreshToken(requestRefreshToken));
            })
        .orElseThrow(
            () ->
                new TokenRefreshException(
                    requestRefreshToken, "Refresh token is not in database!"));
  }

  @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseDataDto> logoutUser(@RequestParam Integer userId) {
    refreshTokenService.deleteByUserId(userId);
    ResponseDataDto responseDataDto = new ResponseDataDto();
    responseDataDto.setData("Log out successful!");
    return ResponseEntity.ok(responseDataDto);
  }

  @PostMapping(
      value = "/register",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseDataDto> registerUser(
      @Valid @RequestBody RegisterRequest signUpRequest) {
    ResponseDataDto responseDataDto = new ResponseDataDto();
    // Check if username is already taken
    if (Boolean.TRUE.equals(userService.existsByUsername(signUpRequest.username()))) {
      responseDataDto.setData("Error: Username is already taken!");
      return ResponseEntity.badRequest().body(responseDataDto);
    }
    // Check if email is already in use
    if (Boolean.TRUE.equals(userService.existsByEmail(signUpRequest.email()))) {
      responseDataDto.setData("Error: Email is already in use!");
      return ResponseEntity.badRequest().body(responseDataDto);
    }

    Set<String> strRoles = signUpRequest.roles();
    Set<RoleDto> roles = new HashSet<>();
    if (strRoles == null || strRoles.isEmpty()) {
      // Default role is RECEPTIONIST if not specified
      RoleDto roleDto =
          roleService
              .findByName("CUSTOMER")
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
    CustomerDto customerDto = customerService.saveCustomer(customerRegister);
    responseDataDto.setData(customerDto);
    return ResponseEntity.ok(responseDataDto);
  }
}
