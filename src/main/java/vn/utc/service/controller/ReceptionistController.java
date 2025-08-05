package vn.utc.service.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.utc.service.config.ContsConfig;
import vn.utc.service.config.JwtTokenProvider;
import vn.utc.service.dtos.*;
import vn.utc.service.entity.Customer;
import vn.utc.service.entity.Vehicle;
import vn.utc.service.mapper.CustomerMapper;
import vn.utc.service.mapper.VehicleMapper;
import vn.utc.service.service.AuthService;
import vn.utc.service.service.CustomerService;
import vn.utc.service.service.UserService;
import vn.utc.service.service.VehicleService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/receptionist")
@RequiredArgsConstructor
public class ReceptionistController {

  private final CustomerService customerService;
  private final VehicleService vehicleService;
  private final CustomerMapper customerMapper;
  private final VehicleMapper vehicleMapper;
  private final JwtTokenProvider jwtTokenProvider;
  private final UserService userService;
  private final AuthService authService;

  @PostMapping("/customers-with-vehicle")
  public ResponseEntity<ResponseDataDto> createCustomerWithVehicle(
      @Valid @RequestBody CustomerWithVehicleRequest customerWithVehicleRequest,
      HttpServletRequest request) {
    ResponseDataDto responseDataDto = new ResponseDataDto();
    List<String> roles = jwtTokenProvider.getRolesFromRequest(request);
    if (roles.isEmpty() || !roles.contains(ContsConfig.RECEPTIONIST)) {
      responseDataDto.setErrorMessage("Unauthorized access");
      responseDataDto.setErrorCode("99");
      return ResponseEntity.status(403).body(responseDataDto);
    }

    String username = customerWithVehicleRequest.getRegisterRequest().username();
    if (Boolean.TRUE.equals(userService.existsByUsername(username))) {
      responseDataDto.setData("Error: Username is already taken!");
      return ResponseEntity.badRequest().body(responseDataDto);
    }

    // 2. Save customer
    CustomerDto customerDto =
        authService.registerCustomer(customerWithVehicleRequest.getRegisterRequest());
    Customer customerEntity = customerMapper.toEntity(customerDto);

    // 3. Map VehicleDto to Vehicle entity and set customer
    Vehicle vehicleEntity = vehicleMapper.toEntity(customerWithVehicleRequest.getVehicle());
    vehicleEntity.setCustomer(customerEntity);

    // 4. Save vehicle
    VehicleDto createdVehicle = vehicleService.saveVehicle(vehicleEntity);

    // 5. Return both
    Map<String, Object> response = new HashMap<>();
    response.put("customer", customerDto);
    response.put("vehicle", createdVehicle);
    responseDataDto.setData(response);

    return ResponseEntity.ok(responseDataDto);
  }
}
