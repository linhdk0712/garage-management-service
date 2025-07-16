package vn.utc.service.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.utc.service.config.ContsConfig;
import vn.utc.service.config.JwtTokenProvider;
import vn.utc.service.dtos.*;
import vn.utc.service.service.AppointmentService;
import vn.utc.service.service.CustomerService;
import vn.utc.service.service.VehicleService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/receptionist/appointments")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@Tag(name = "Receptionist Appointments", description = "Receptionist appointment management")
public class ReceptionistAppointmentController {
    private final AppointmentService appointmentService;
    private final CustomerService customerService;
    private final VehicleService vehicleService;
    private final JwtTokenProvider jwtTokenProvider;

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
        List<String> roles = jwtTokenProvider.getRolesFromRequest(request);
        if (roles.isEmpty() || !roles.contains(ContsConfig.RECEPTIONIST)) {
            responseDataDto.setErrorMessage("Unauthorized access");
            responseDataDto.setErrorCode("99");
            return ResponseEntity.status(403).body(responseDataDto);
        }
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<AppointmentDto> appointmentPage = appointmentService.getAllAppointments(pageable, status, from, to, date);
        PaginatedResponseDto<AppointmentDto> paginatedResponse = PaginatedResponseDto.of(
                appointmentPage.getContent(),
                appointmentPage.getNumber(),
                appointmentPage.getSize(),
                appointmentPage.getTotalElements()
        );
        responseDataDto.setData(paginatedResponse);
        return ResponseEntity.ok(responseDataDto);
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<ResponseDataDto> createAppointment(
            @Valid @RequestBody AppointmentDto appointmentDto,
            @RequestParam(required = false) boolean createCustomerIfNotExists,
            HttpServletRequest request) {
        ResponseDataDto responseDataDto = new ResponseDataDto();
        List<String> roles = jwtTokenProvider.getRolesFromRequest(request);
        if (roles.isEmpty() || !roles.contains(ContsConfig.RECEPTIONIST)) {
            responseDataDto.setErrorMessage("Unauthorized access");
            responseDataDto.setErrorCode("99");
            return ResponseEntity.status(403).body(responseDataDto);
        }
        try {
            CustomerDto customerDto = null;
            if (appointmentDto.customerId() != null) {
                customerDto = customerService.findByCustomerUserId(appointmentDto.customerId()).orElse(null);
            }
            if (customerDto == null && createCustomerIfNotExists && appointmentDto.customerRegister() != null) {
                customerDto = customerService.saveCustomer(appointmentDto.customerRegister());
            }
            if (customerDto == null) {
                responseDataDto.setErrorMessage("Customer not found and createCustomerIfNotExists is false");
                responseDataDto.setErrorCode("400");
                return ResponseEntity.badRequest().body(responseDataDto);
            }
            AppointmentDto createdAppointment = appointmentService.createAppointment(appointmentDto, customerDto);
            responseDataDto.setData(createdAppointment);
            return ResponseEntity.ok(responseDataDto);
        } catch (Exception e) {
            responseDataDto.setErrorCode("99");
            responseDataDto.setErrorMessage("Failed to create appointment: " + e.getMessage());
            return ResponseEntity.badRequest().body(responseDataDto);
        }
    }
} 