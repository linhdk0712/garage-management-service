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
import vn.utc.service.dtos.*;
import vn.utc.service.entity.SparePart;
import vn.utc.service.service.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/manager")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@Tag(name = "Manager", description = "Manager information management")
public class ManagerController {
    private final JwtTokenProvider jwtTokenProvider;
    private final StaffProfileService staffProfileService;
    private final AppointmentService appointmentService;
    private final StaffService staffService;
    private final CustomerService customerService;
    private final VehicleService vehicleService;
    private final SparePartService sparePartService;
    private final AnalyticsService analyticsService;

    @GetMapping(value = "/profile/{userName}", produces = "application/json")
    public ResponseEntity<ResponseDataDto> getCustomerInfo(
            @PathVariable String userName, HttpServletRequest request) {
        ResponseDataDto responseDataDto = new ResponseDataDto();
        List<String> roles = jwtTokenProvider.getRolesFromRequest(request);
        if (roles.isEmpty() || !roles.contains(ContsConfig.MANAGER)) {
            responseDataDto.setErrorMessage("Unauthorized access");
            responseDataDto.setErrorCode("99");
            return ResponseEntity.status(403).body(responseDataDto);
        }

        try {
            StaffProfileDto staffProfileDto = staffProfileService.findStaffProfileByUsername(userName)
                    .orElseThrow(() -> new RuntimeException("Staff profile not found"));
            responseDataDto.setData(staffProfileDto);
            return ResponseEntity.ok(responseDataDto);
        } catch (Exception e) {
            responseDataDto.setErrorMessage("Error fetching staff profile: " + e.getMessage());
            responseDataDto.setErrorCode("500");
            return ResponseEntity.status(500).body(responseDataDto);
        }
    }

    @GetMapping(value = "/appointments", produces = "application/json")
    public ResponseEntity<ResponseDataDto> getAppointments(
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
        if (roles.isEmpty() || !roles.contains(ContsConfig.MANAGER)) {
            responseDataDto.setErrorMessage("Unauthorized access");
            responseDataDto.setErrorCode("99");
            return ResponseEntity.status(403).body(responseDataDto);
        }

        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
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
        } catch (Exception e) {
            responseDataDto.setErrorMessage("Error fetching appointments: " + e.getMessage());
            responseDataDto.setErrorCode("500");
            return ResponseEntity.status(500).body(responseDataDto);
        }
    }

    @GetMapping(value = "/staff", produces = "application/json")
    public ResponseEntity<ResponseDataDto> getStaffs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            HttpServletRequest request) {
        ResponseDataDto responseDataDto = new ResponseDataDto();
        List<String> roles = jwtTokenProvider.getRolesFromRequest(request);
        if (roles.isEmpty() || !roles.contains(ContsConfig.MANAGER)) {
            responseDataDto.setErrorMessage("Unauthorized access");
            responseDataDto.setErrorCode("99");
            return ResponseEntity.status(403).body(responseDataDto);
        }

        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<StaffDto> staffPage = staffService.findAll(pageable);
            PaginatedResponseDto<StaffDto> paginatedResponse = PaginatedResponseDto.of(
                staffPage.getContent(),
                staffPage.getNumber(),
                staffPage.getSize(),
                staffPage.getTotalElements()
            );
            
            responseDataDto.setData(paginatedResponse);
            return ResponseEntity.ok(responseDataDto);
        } catch (Exception e) {
            responseDataDto.setErrorMessage("Error fetching staff: " + e.getMessage());
            responseDataDto.setErrorCode("500");
            return ResponseEntity.status(500).body(responseDataDto);
        }
    }

    @GetMapping(value = "/customers", produces = "application/json")
    public ResponseEntity<ResponseDataDto> getCustomer(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            HttpServletRequest request) {
        ResponseDataDto responseDataDto = new ResponseDataDto();
        List<String> roles = jwtTokenProvider.getRolesFromRequest(request);
        if (roles.isEmpty() || !roles.contains(ContsConfig.MANAGER)) {
            responseDataDto.setErrorMessage("Unauthorized access");
            responseDataDto.setErrorCode("99");
            return ResponseEntity.status(403).body(responseDataDto);
        }

        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<CustomerDto> customerPage = customerService.findAllCustomers(pageable, search, status);
            PaginatedResponseDto<CustomerDto> paginatedResponse = PaginatedResponseDto.of(
                customerPage.getContent(),
                customerPage.getNumber(),
                customerPage.getSize(),
                customerPage.getTotalElements()
            );
            
            responseDataDto.setData(paginatedResponse);
            return ResponseEntity.ok(responseDataDto);
        } catch (Exception e) {
            responseDataDto.setErrorMessage("Error fetching customers: " + e.getMessage());
            responseDataDto.setErrorCode("500");
            return ResponseEntity.status(500).body(responseDataDto);
        }
    }

    @GetMapping(value = "/vehicles", produces = "application/json")
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
        if (roles.isEmpty() || !roles.contains(ContsConfig.MANAGER)) {
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

    @GetMapping(value = "/reports/dashboard", produces = "application/json")
    public ResponseEntity<ResponseDataDto> getDashboardStats(
            @RequestParam(defaultValue = "week") String period,
            HttpServletRequest request) {
        ResponseDataDto responseDataDto = new ResponseDataDto();
        List<String> roles = jwtTokenProvider.getRolesFromRequest(request);
        if (roles.isEmpty() || !roles.contains(ContsConfig.MANAGER)) {
            responseDataDto.setErrorMessage("Unauthorized access");
            responseDataDto.setErrorCode("99");
            return ResponseEntity.status(403).body(responseDataDto);
        }

        try {
            Map<String, Object> dashboardStats = analyticsService.getDashboardStats();
            responseDataDto.setData(dashboardStats);
            return ResponseEntity.ok(responseDataDto);
        } catch (Exception e) {
            responseDataDto.setErrorMessage("Error fetching dashboard stats: " + e.getMessage());
            responseDataDto.setErrorCode("500");
            return ResponseEntity.status(500).body(responseDataDto);
        }
    }

    @GetMapping(value = "/inventory/low-stock", produces = "application/json")
    public ResponseEntity<ResponseDataDto> getLowStockItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            HttpServletRequest request) {
        ResponseDataDto responseDataDto = new ResponseDataDto();
        List<String> roles = jwtTokenProvider.getRolesFromRequest(request);
        if (roles.isEmpty() || !roles.contains(ContsConfig.MANAGER)) {
            responseDataDto.setErrorMessage("Unauthorized access");
            responseDataDto.setErrorCode("99");
            return ResponseEntity.status(403).body(responseDataDto);
        }

        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<SparePart> lowStockPage = sparePartService.findLowStockItems(pageable);
            PaginatedResponseDto<SparePart> paginatedResponse = PaginatedResponseDto.of(
                lowStockPage.getContent(),
                lowStockPage.getNumber(),
                lowStockPage.getSize(),
                lowStockPage.getTotalElements()
            );
            
            responseDataDto.setData(paginatedResponse);
            return ResponseEntity.ok(responseDataDto);
        } catch (Exception e) {
            responseDataDto.setErrorMessage("Error fetching low stock items: " + e.getMessage());
            responseDataDto.setErrorCode("500");
            return ResponseEntity.status(500).body(responseDataDto);
        }
    }

    @GetMapping(value = "/reports/customers", produces = "application/json")
    public ResponseEntity<ResponseDataDto> getCustomerStatistics(
            @RequestParam(defaultValue = "MONTH") String period,
            HttpServletRequest request) {
        ResponseDataDto responseDataDto = new ResponseDataDto();
        List<String> roles = jwtTokenProvider.getRolesFromRequest(request);
        if (roles.isEmpty() || !roles.contains(ContsConfig.MANAGER)) {
            responseDataDto.setErrorMessage("Unauthorized access");
            responseDataDto.setErrorCode("99");
            return ResponseEntity.status(403).body(responseDataDto);
        }

        try {
            Map<String, Object> customerStats = analyticsService.getCustomerAnalytics();
            responseDataDto.setData(customerStats);
            return ResponseEntity.ok(responseDataDto);
        } catch (Exception e) {
            responseDataDto.setErrorMessage("Error fetching customer statistics: " + e.getMessage());
            responseDataDto.setErrorCode("500");
            return ResponseEntity.status(500).body(responseDataDto);
        }
    }

    @GetMapping(value = "/analytics/performance", produces = "application/json")
    public ResponseEntity<ResponseDataDto> getPerformanceAnalytics(
            @RequestParam(defaultValue = "month") String period,
            HttpServletRequest request) {
        ResponseDataDto responseDataDto = new ResponseDataDto();
        List<String> roles = jwtTokenProvider.getRolesFromRequest(request);
        if (roles.isEmpty() || !roles.contains(ContsConfig.MANAGER)) {
            responseDataDto.setErrorMessage("Unauthorized access");
            responseDataDto.setErrorCode("99");
            return ResponseEntity.status(403).body(responseDataDto);
        }

        try {
            Map<String, Object> analyticsData = analyticsService.getPerformanceAnalytics(period);
            responseDataDto.setData(analyticsData);
            return ResponseEntity.ok(responseDataDto);
        } catch (Exception e) {
            responseDataDto.setErrorMessage("Error fetching performance analytics: " + e.getMessage());
            responseDataDto.setErrorCode("500");
            return ResponseEntity.status(500).body(responseDataDto);
        }
    }
}
