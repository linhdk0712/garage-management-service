package vn.utc.service.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.utc.service.config.ContsConfig;
import vn.utc.service.config.JwtTokenProvider;
import vn.utc.service.dtos.CustomerDto;
import vn.utc.service.dtos.CustomerProfileDto;
import vn.utc.service.dtos.PaginatedResponseDto;
import vn.utc.service.dtos.ResponseDataDto;
import vn.utc.service.entity.CustomerProfile;
import vn.utc.service.service.CustomerProfileService;
import vn.utc.service.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@Tag(name = "Customers", description = "Customers information management")
public class CustomerController {

  private final JwtTokenProvider jwtTokenProvider;
  private final CustomerProfileService customerProfileService;
  private final CustomerService customerService;

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
  @GetMapping(value = "/{id}", produces = "application/json")
  public ResponseEntity<ResponseDataDto> getCustomerById(@PathVariable Integer id, HttpServletRequest request) {
    ResponseDataDto responseDataDto = new ResponseDataDto();

    CustomerDto customerDto = customerService.findByCustomerUserId(id).orElse(null);
    // Optionally, add role checks here if you want to restrict access
      assert customerDto != null;
      CustomerProfileDto customerProfile = customerProfileService.findCustomerProfileById(customerDto.user().getId()).orElse(null);
    if (customerProfile == null) {
      responseDataDto.setErrorCode("404");
      responseDataDto.setErrorMessage("Customer not found");
      return ResponseEntity.status(404).body(responseDataDto);
    }
    responseDataDto.setData(customerProfile);
    return ResponseEntity.ok(responseDataDto);
  }
  @GetMapping(value = "/profile", produces = "application/json")
  public ResponseEntity<ResponseDataDto> getCustomerProfile(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "asc") String sortDir,
      @RequestParam(required = false) String search,
      @RequestParam(required = false) String status,
      HttpServletRequest request) {
    // Implementation to retrieve customer profile with pagination
    ResponseDataDto responseDataDto = new ResponseDataDto();
    List<String> roles = jwtTokenProvider.getRolesFromRequest(request);
    if (roles.isEmpty() || !roles.contains(ContsConfig.MANAGER)) {
      responseDataDto.setErrorMessage("Unauthorized access");
      responseDataDto.setErrorCode("99");
      return ResponseEntity.status(403).body(responseDataDto);
    }

    Sort sort = sortDir.equalsIgnoreCase("desc") ? 
        Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
    Pageable pageable = PageRequest.of(page, size, sort);
    
    Page<CustomerProfileDto> customerProfilePage = customerProfileService.findAllCustomerProfiles(
        pageable, search, status);
    
    PaginatedResponseDto<CustomerProfileDto> paginatedResponse = PaginatedResponseDto.of(
        customerProfilePage.getContent(),
        customerProfilePage.getNumber(),
        customerProfilePage.getSize(),
        customerProfilePage.getTotalElements()
    );

    responseDataDto.setData(paginatedResponse);
    return ResponseEntity.ok(responseDataDto);
  }
}
