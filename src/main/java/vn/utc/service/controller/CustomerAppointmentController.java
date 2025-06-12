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
import vn.utc.service.config.JwtTokenProvider;
import vn.utc.service.dtos.AppointmentDto;
import vn.utc.service.dtos.CustomerDto;
import vn.utc.service.dtos.CustomerDashboardDto;
import vn.utc.service.dtos.PaginatedResponseDto;
import vn.utc.service.dtos.ResponseDataDto;
import vn.utc.service.dtos.VehicleDto;
import vn.utc.service.service.AppointmentService;
import vn.utc.service.service.CustomerService;
import vn.utc.service.service.VehicleService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers/appointments")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@Tag(name = "Vehicle", description = "Appointments management")
public class CustomerAppointmentController {

    private final AppointmentService appointmentService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomerService customerService;
    private final VehicleService vehicleService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<ResponseDataDto> getAllAppointments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(required = false) String date,
            HttpServletRequest request) {
        ResponseDataDto responseDataDto = new ResponseDataDto();
        String userName = jwtTokenProvider.getUsernameFromRequest(request);
        Integer customerId = customerService.finByUserName(userName)
                .map(customerDto -> customerDto.id())
                .orElseThrow(() -> new RuntimeException("Customer not found for user: " + userName));
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<AppointmentDto> appointmentPage = appointmentService.getAllAppointments(
            customerId, pageable, status, from, to, date);
        PaginatedResponseDto<AppointmentDto> paginatedResponse = PaginatedResponseDto.of(
            appointmentPage.getContent(),
            appointmentPage.getNumber(),
            appointmentPage.getSize(),
            appointmentPage.getTotalElements()
        );
        
        responseDataDto.setData(paginatedResponse);
        return ResponseEntity.ok(responseDataDto);
    }
    
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<ResponseDataDto> getAppointmentById(@PathVariable Integer id) {
        ResponseDataDto responseDataDto = new ResponseDataDto();
        appointmentService.findById(id).ifPresentOrElse(
                responseDataDto::setData,
                () -> responseDataDto.setErrorCode("99").setErrorMessage("Appointment not found")
        );
        return ResponseEntity.ok(responseDataDto);
    }
    
    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<ResponseDataDto> createAppointment(@RequestBody AppointmentDto appointmentDto, HttpServletRequest request) {
        ResponseDataDto responseDataDto = new ResponseDataDto();
        String userName = jwtTokenProvider.getUsernameFromRequest(request);
        CustomerDto customerDto = customerService.finByUserName(userName)
                .orElseThrow(() -> new RuntimeException("Customer not found for user: " + userName));
        
        // Validate appointment data
        if (appointmentDto.appointmentDate() == null) {
            responseDataDto.setErrorCode("99");
            responseDataDto.setErrorMessage("Appointment date is required");
            return ResponseEntity.badRequest().body(responseDataDto);
        }
        
        if (appointmentDto.serviceType() == null || appointmentDto.serviceType().trim().isEmpty()) {
            responseDataDto.setErrorCode("99");
            responseDataDto.setErrorMessage("Service type is required");
            return ResponseEntity.badRequest().body(responseDataDto);
        }
        
        // Check if appointment date is in the future
        if (appointmentDto.appointmentDate().isBefore(java.time.Instant.now())) {
            responseDataDto.setErrorCode("99");
            responseDataDto.setErrorMessage("Appointment date must be in the future");
            return ResponseEntity.badRequest().body(responseDataDto);
        }
        
        try {
            AppointmentDto createdAppointment = appointmentService.createAppointment(appointmentDto, customerDto);
            responseDataDto.setData(createdAppointment);
            return ResponseEntity.ok(responseDataDto);
        } catch (Exception e) {
            responseDataDto.setErrorCode("99");
            responseDataDto.setErrorMessage("Failed to create appointment: " + e.getMessage());
            return ResponseEntity.badRequest().body(responseDataDto);
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ResponseDataDto> getCustomerDashboard(HttpServletRequest request) {
        ResponseDataDto responseDataDto = new ResponseDataDto();
        String userName = jwtTokenProvider.getUsernameFromRequest(request);
        Integer customerId = customerService.finByUserName(userName)
                .map(customerDto -> customerDto.id())
                .orElseThrow(() -> new RuntimeException("Customer not found for user: " + userName));
        
        // Get all appointments for the customer (without pagination for dashboard)
        List<AppointmentDto> appointments = appointmentService.getAllAppointments(customerId);
        
        // Get all vehicles for the customer
        List<VehicleDto> vehicles = vehicleService.getVehiclesByCustomerId(customerId);
        
        CustomerDashboardDto dashboardData = new CustomerDashboardDto(appointments, vehicles);
        responseDataDto.setData(dashboardData);
        return ResponseEntity.ok(responseDataDto);
    }
}
