package vn.utc.service.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.utc.service.config.ContsConfig;
import vn.utc.service.config.JwtTokenProvider;
import vn.utc.service.dtos.CustomerProfileDto;
import vn.utc.service.dtos.ResponseDataDto;
import vn.utc.service.service.CustomerProfileService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@Tag(name = "Customers", description = "Customers information management")
public class CustomerController {

  private final JwtTokenProvider jwtTokenProvider;
  private final CustomerProfileService customerProfileService;

  @GetMapping(value = "/profile/{userName}", produces = "application/json")
  public ResponseEntity<ResponseDataDto> getCustomerInfo(
      @PathVariable String userName, HttpServletRequest request) {
    // Implementation to retrieve customer information
    ResponseDataDto responseDataDto = new ResponseDataDto();
    String userNameRequest = jwtTokenProvider.getUsernameFromRequest(request);
    if (userNameRequest == null || !userNameRequest.equals(userName)) {
      responseDataDto.setErrorMessage("Unauthorized access");
      responseDataDto.setErrorCode("99");
      return ResponseEntity.status(403).body(responseDataDto);
    }
    CustomerProfileDto customerProfileDto =
        customerProfileService.findCustomerProfileByUsername(userName).orElse(null);
    responseDataDto.setData(customerProfileDto);
    return ResponseEntity.ok(responseDataDto);
  }

  @GetMapping(value = "/profile", produces = "application/json")
  public ResponseEntity<ResponseDataDto> getCustomerProfile(HttpServletRequest request) {
    // Implementation to retrieve customer profile
    ResponseDataDto responseDataDto = new ResponseDataDto();
    List<String> roles = jwtTokenProvider.getRolesFromRequest(request);
    if (roles.isEmpty() || !roles.contains(ContsConfig.MANAGER)) {
      responseDataDto.setErrorMessage("Unauthorized access");
      responseDataDto.setErrorCode("99");
      return ResponseEntity.status(403).body(responseDataDto);
    }

    List<CustomerProfileDto> customerProfileDtos = customerProfileService.findAllCustomerProfiles();

    responseDataDto.setData(customerProfileDtos);
    return ResponseEntity.ok(responseDataDto);
  }
}
