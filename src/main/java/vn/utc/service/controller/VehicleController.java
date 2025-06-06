package vn.utc.service.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.utc.service.config.JwtTokenProvider;
import vn.utc.service.dtos.CustomerDto;
import vn.utc.service.dtos.ResponseDataDto;
import vn.utc.service.dtos.UserDto;
import vn.utc.service.dtos.VehicleDto;
import vn.utc.service.service.CustomerService;
import vn.utc.service.service.UserService;
import vn.utc.service.service.VehicleService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/vehicles")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@Tag(name = "Vehicle", description = "Vehicle management")
public class VehicleController {

  private final VehicleService vehicleService;
  private final JwtTokenProvider jwtTokenProvider;
  private final UserService userService;
  private final CustomerService customerService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseDataDto> getAllVehicles(HttpServletRequest request) {
    String roles = jwtTokenProvider.getRolesFromRequest(request).get(0);
    String userName = jwtTokenProvider.getUsernameFromRequest(request);
    UserDto userDto = userService.findByUsername(userName).orElse(null);
    ResponseDataDto responseDataDto = new ResponseDataDto();
    if (roles.equalsIgnoreCase("CUSTOMER")) {
      CustomerDto customerDto = customerService.findByCustomerId(userDto.getId()).orElse(null);
      List<VehicleDto> vehicleDtos = vehicleService.getVehiclesByCustomerId(customerDto.id());
      responseDataDto.setData(vehicleDtos);
    } else {
      List<VehicleDto> vehicleDtos = vehicleService.getAllVehicles();
      responseDataDto.setData(vehicleDtos);
    }

    return ResponseEntity.ok(responseDataDto);
  }
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseDataDto> createVehicle(@Valid @RequestBody VehicleDto vehicleDto,HttpServletRequest request) {
    ResponseDataDto responseDataDto = new ResponseDataDto();
    String userName = jwtTokenProvider.getUsernameFromRequest(request);
    responseDataDto.setData(vehicleService.createVehicle(vehicleDto, userName));
    return ResponseEntity.ok(responseDataDto);
  }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDataDto> getVehicleById(@PathVariable Integer id) {
        ResponseDataDto responseDataDto = new ResponseDataDto();
        Optional<VehicleDto> vehicleDto = vehicleService.getVehicleById(id);
        responseDataDto.setData(vehicleDto.orElse(null));
        return ResponseEntity.ok(responseDataDto);
    }
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDataDto> updateVehicle(@PathVariable Integer id, @Valid @RequestBody VehicleDto vehicleDto) {
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setData(vehicleService.updateVehicle(id, vehicleDto));
        return ResponseEntity.ok(responseDataDto);
    }
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDataDto> deleteVehicle(@PathVariable Integer id) {
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setData(null);
        return ResponseEntity.ok(responseDataDto);
    }
}
