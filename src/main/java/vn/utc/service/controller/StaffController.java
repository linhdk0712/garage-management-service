package vn.utc.service.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.utc.service.config.ContsConfig;
import vn.utc.service.config.JwtTokenProvider;
import vn.utc.service.dtos.*;
import vn.utc.service.service.StaffService;
import vn.utc.service.service.WorkOrderService;
import vn.utc.service.service.AppointmentService;
import vn.utc.service.service.VehicleService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/staff")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@Tag(name = "Staff", description = "Staff information management")
public class StaffController {
    private final JwtTokenProvider jwtTokenProvider;
    private  final StaffService staffService;
    private final WorkOrderService workOrderService;
    private final AppointmentService appointmentService;
    private final VehicleService vehicleService;

    @PostMapping(produces = "application/json",consumes = "application/json")
    public ResponseEntity<ResponseDataDto> create(@Valid @RequestBody StaffRequest registerRequest, HttpServletRequest request) {
        ResponseDataDto responseDataDto = new ResponseDataDto();
        List<String> roles = jwtTokenProvider.getRolesFromRequest(request);
        if (roles.isEmpty() || !roles.contains(ContsConfig.MANAGER)) {
            responseDataDto.setErrorMessage("Unauthorized access");
            responseDataDto.setErrorCode("99");
            return ResponseEntity.status(403).body(responseDataDto);
        }
        StaffDto createdStaff = staffService.createStaff(registerRequest)
                .orElseThrow(() -> new RuntimeException("Failed to create staff"));
        responseDataDto.setData(createdStaff);
        return ResponseEntity.ok(responseDataDto);

    }
     // New endpoints for staff work orders
     @GetMapping("/work-orders")
     public ResponseEntity<ResponseDataDto> getWorkOrders(
             @RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "10") int size,
             @RequestParam(required = false) String status,
             @RequestParam(required = false) String from,
             @RequestParam(required = false) String to,
             HttpServletRequest request) {
         ResponseDataDto responseDataDto = new ResponseDataDto();
         List<String> roles = jwtTokenProvider.getRolesFromRequest(request);
         if (roles.isEmpty() || !roles.contains(ContsConfig.STAFF)) {
             responseDataDto.setErrorMessage("Unauthorized access");
             responseDataDto.setErrorCode("99");
             return ResponseEntity.status(403).body(responseDataDto);
         }
        String userName = jwtTokenProvider.getUsernameFromRequest(request);
        Integer staffId = staffService.findByUser(userName)
                .map(staffDto -> staffDto.id())
                .orElseThrow(() -> new RuntimeException("Staff not found for user: " + userName));
         Pageable pageable = PageRequest.of(page, size);
         Page<WorkOrderDto> workOrders = workOrderService.getWorkOrdersByStaffId(staffId, pageable, status, from, to);
         responseDataDto.setData(workOrders);
         return ResponseEntity.ok(responseDataDto);
     }
 
     // New endpoints for staff appointments
     @GetMapping("/appointments")
     public ResponseEntity<ResponseDataDto> getAppointments(
             @RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "10") int size,
             @RequestParam(required = false) String status,
             @RequestParam(required = false) String from,
             @RequestParam(required = false) String to,
             HttpServletRequest request) {
         ResponseDataDto responseDataDto = new ResponseDataDto();
         List<String> roles = jwtTokenProvider.getRolesFromRequest(request);
         if (roles.isEmpty() || !roles.contains(ContsConfig.STAFF)) {
             responseDataDto.setErrorMessage("Unauthorized access");
             responseDataDto.setErrorCode("99");
             return ResponseEntity.status(403).body(responseDataDto);
         }
         String userName = jwtTokenProvider.getUsernameFromRequest(request);
        Integer staffId = staffService.findByUser(userName)
                .map(staffDto -> staffDto.id())
                .orElseThrow(() -> new RuntimeException("Staff not found for user: " + userName));
         Pageable pageable = PageRequest.of(page, size);
         Page<AppointmentDto> appointments = appointmentService.getAppointmentsByStaffId(staffId, pageable, status, from, to);
         responseDataDto.setData(appointments);
         return ResponseEntity.ok(responseDataDto);
     }

    // New endpoint for staff vehicles
    @GetMapping("/vehicles")
    public ResponseEntity<ResponseDataDto> getVehicles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String make,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer customerId,
            HttpServletRequest request) {
        ResponseDataDto responseDataDto = new ResponseDataDto();
        List<String> roles = jwtTokenProvider.getRolesFromRequest(request);
        if (roles.isEmpty() || !roles.contains(ContsConfig.STAFF)) {
            responseDataDto.setErrorMessage("Unauthorized access");
            responseDataDto.setErrorCode("99");
            return ResponseEntity.status(403).body(responseDataDto);
        }

        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<VehicleDto> vehiclePage = vehicleService.getAllVehicles(pageable, search, make, model, year, customerId);
            PaginatedResponseDto<VehicleDto> paginatedResponse = PaginatedResponseDto.of(
                vehiclePage.getContent(),
                vehiclePage.getNumber(),
                vehiclePage.getSize(),
                vehiclePage.getTotalElements()
            );
            
            responseDataDto.setData(paginatedResponse);
            return ResponseEntity.ok(responseDataDto);
        } catch (Exception e) {
            responseDataDto.setErrorMessage("Error fetching vehicles: " + e.getMessage());
            responseDataDto.setErrorCode("500");
            return ResponseEntity.status(500).body(responseDataDto);
        }
    }
}
