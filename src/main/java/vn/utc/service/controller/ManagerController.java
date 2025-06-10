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
import vn.utc.service.service.*;
import vn.utc.service.entity.SparePart;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

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
        List<String> roles = jwtTokenProvider.getRolesFromRequest(request);
        if (roles.isEmpty() || !roles.contains(ContsConfig.MANAGER)) {
            responseDataDto.setErrorMessage("Unauthorized access");
            responseDataDto.setErrorCode("99");
            return ResponseEntity.status(403).body(responseDataDto);
        }
        StaffProfileDto staffProfileDto =
                staffProfileService.findStaffProfileByUsername(userName).orElse(null);
        responseDataDto.setData(staffProfileDto);
        return ResponseEntity.ok(responseDataDto);
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
            Map<String, Object> dashboardStats = new HashMap<>();
            
            // Mock data for dashboard stats - replace with actual service calls
            Map<String, Object> revenue = new HashMap<>();
            revenue.put("today", 1250.0);
            revenue.put("yesterday", 1100.0);
            revenue.put("thisWeek", 8750.0);
            revenue.put("lastWeek", 8200.0);
            revenue.put("thisMonth", 35000.0);
            revenue.put("lastMonth", 32000.0);
            revenue.put("changePercentage", 6.7);
            dashboardStats.put("revenue", revenue);

            Map<String, Object> customers = new HashMap<>();
            customers.put("new", 12);
            customers.put("active", 45);
            customers.put("total", 156);
            customers.put("changePercentage", 8.5);
            dashboardStats.put("customers", customers);

            Map<String, Object> appointments = new HashMap<>();
            appointments.put("today", 8);
            appointments.put("pending", 3);
            appointments.put("confirmed", 4);
            appointments.put("inProgress", 1);
            appointments.put("completed", 0);
            appointments.put("cancelled", 0);
            dashboardStats.put("appointments", appointments);

            Map<String, Object> workOrders = new HashMap<>();
            workOrders.put("open", 15);
            workOrders.put("completed", 28);
            workOrders.put("changePercentage", 12.3);
            dashboardStats.put("workOrders", workOrders);

            Map<String, Object> inventory = new HashMap<>();
            inventory.put("lowStockCount", 5);
            inventory.put("totalValue", 125000.0);
            List<Map<String, Object>> mostUsedParts = List.of(
                Map.of("name", "Oil Filter", "count", 45),
                Map.of("name", "Brake Pads", "count", 32),
                Map.of("name", "Air Filter", "count", 28)
            );
            inventory.put("mostUsedParts", mostUsedParts);
            dashboardStats.put("inventory", inventory);

            List<Map<String, Object>> servicesByType = List.of(
                Map.of("name", "Oil Change", "value", 25),
                Map.of("name", "Brake Service", "value", 18),
                Map.of("name", "Tire Rotation", "value", 15),
                Map.of("name", "Engine Tune-up", "value", 12),
                Map.of("name", "Other", "value", 30)
            );
            dashboardStats.put("servicesByType", servicesByType);

            List<Map<String, Object>> revenueByDay = List.of(
                Map.of("name", "Mon", "value", 1200),
                Map.of("name", "Tue", "value", 1350),
                Map.of("name", "Wed", "value", 1100),
                Map.of("name", "Thu", "value", 1400),
                Map.of("name", "Fri", "value", 1600),
                Map.of("name", "Sat", "value", 1800),
                Map.of("name", "Sun", "value", 700)
            );
            dashboardStats.put("revenueByDay", revenueByDay);

            List<Map<String, Object>> staffPerformance = List.of(
                Map.of("name", "John Smith", "completed", 12, "hours", 40),
                Map.of("name", "Mike Johnson", "completed", 10, "hours", 38),
                Map.of("name", "Sarah Wilson", "completed", 8, "hours", 35),
                Map.of("name", "David Brown", "completed", 6, "hours", 32)
            );
            dashboardStats.put("staffPerformance", staffPerformance);

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
            Map<String, Object> analyticsData = new HashMap<>();
            
            // Mock revenue data
            Map<String, Object> revenue = new HashMap<>();
            List<Map<String, Object>> dailyRevenue = List.of(
                Map.of("date", "2024-01-01", "amount", 1200),
                Map.of("date", "2024-01-02", "amount", 1350),
                Map.of("date", "2024-01-03", "amount", 1100),
                Map.of("date", "2024-01-04", "amount", 1400),
                Map.of("date", "2024-01-05", "amount", 1600),
                Map.of("date", "2024-01-06", "amount", 1800),
                Map.of("date", "2024-01-07", "amount", 700)
            );
            revenue.put("daily", dailyRevenue);
            
            List<Map<String, Object>> weeklyRevenue = List.of(
                Map.of("week", "Week 1", "amount", 8750),
                Map.of("week", "Week 2", "amount", 9200),
                Map.of("week", "Week 3", "amount", 8900),
                Map.of("week", "Week 4", "amount", 9500)
            );
            revenue.put("weekly", weeklyRevenue);
            
            List<Map<String, Object>> monthlyRevenue = List.of(
                Map.of("month", "Jan", "amount", 35000),
                Map.of("month", "Feb", "amount", 38000),
                Map.of("month", "Mar", "amount", 42000)
            );
            revenue.put("monthly", monthlyRevenue);
            analyticsData.put("revenue", revenue);

            // Mock customers data
            Map<String, Object> customers = new HashMap<>();
            customers.put("newCustomers", 25);
            customers.put("returningCustomers", 120);
            customers.put("customerSatisfaction", 4.5);
            List<Map<String, Object>> customersByService = List.of(
                Map.of("service", "Oil Change", "count", 45),
                Map.of("service", "Brake Service", "count", 32),
                Map.of("service", "Tire Rotation", "count", 28),
                Map.of("service", "Engine Tune-up", "count", 15)
            );
            customers.put("customersByService", customersByService);
            analyticsData.put("customers", customers);

            // Mock repairs data
            Map<String, Object> repairs = new HashMap<>();
            repairs.put("totalRepairs", 156);
            repairs.put("avgCompletionTime", 2.5);
            List<Map<String, Object>> repairsByType = List.of(
                Map.of("type", "Maintenance", "count", 85),
                Map.of("type", "Repair", "count", 45),
                Map.of("type", "Inspection", "count", 26)
            );
            repairs.put("repairsByType", repairsByType);
            List<Map<String, Object>> repairStatus = List.of(
                Map.of("status", "Completed", "count", 120),
                Map.of("status", "In Progress", "count", 25),
                Map.of("status", "Pending", "count", 11)
            );
            repairs.put("repairStatus", repairStatus);
            analyticsData.put("repairs", repairs);

            // Mock inventory data
            Map<String, Object> inventory = new HashMap<>();
            List<Map<String, Object>> partsUsage = List.of(
                Map.of("part", "Oil Filter", "count", 45),
                Map.of("part", "Brake Pads", "count", 32),
                Map.of("part", "Air Filter", "count", 28),
                Map.of("part", "Spark Plugs", "count", 15)
            );
            inventory.put("partsUsage", partsUsage);
            inventory.put("lowStockItems", 5);
            inventory.put("inventoryValue", 125000.0);
            analyticsData.put("inventory", inventory);

            // Mock staff performance data
            Map<String, Object> staffPerformance = new HashMap<>();
            List<Map<String, Object>> staffUtilization = List.of(
                Map.of("staff", "John Smith", "utilization", 85),
                Map.of("staff", "Mike Johnson", "utilization", 78),
                Map.of("staff", "Sarah Wilson", "utilization", 92),
                Map.of("staff", "David Brown", "utilization", 75)
            );
            staffPerformance.put("staffUtilization", staffUtilization);
            List<Map<String, Object>> avgTimePerRepair = List.of(
                Map.of("staff", "John Smith", "time", 2.1),
                Map.of("staff", "Mike Johnson", "time", 2.3),
                Map.of("staff", "Sarah Wilson", "time", 1.9),
                Map.of("staff", "David Brown", "time", 2.5)
            );
            staffPerformance.put("avgTimePerRepair", avgTimePerRepair);
            analyticsData.put("staffPerformance", staffPerformance);

            responseDataDto.setData(analyticsData);
            return ResponseEntity.ok(responseDataDto);
        } catch (Exception e) {
            responseDataDto.setErrorMessage("Error fetching performance analytics: " + e.getMessage());
            responseDataDto.setErrorCode("500");
            return ResponseEntity.status(500).body(responseDataDto);
        }
    }

}
