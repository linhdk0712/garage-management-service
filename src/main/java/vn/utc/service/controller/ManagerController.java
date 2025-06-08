package vn.utc.service.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.utc.service.config.ContsConfig;
import vn.utc.service.config.JwtTokenProvider;
import vn.utc.service.dtos.*;
import vn.utc.service.service.AppointmentService;
import vn.utc.service.service.StaffProfileService;
import vn.utc.service.service.StaffService;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<ResponseDataDto> getAppointments(HttpServletRequest request) {
        ResponseDataDto responseDataDto = new ResponseDataDto();
        List<String> roles = jwtTokenProvider.getRolesFromRequest(request);
        if (roles.isEmpty() || !roles.contains(ContsConfig.MANAGER)) {
            responseDataDto.setErrorMessage("Unauthorized access");
            responseDataDto.setErrorCode("99");
            return ResponseEntity.status(403).body(responseDataDto);
        }
        List<AppointmentDto> appointmentDtoList = appointmentService.getAllAppointments();
        responseDataDto.setData(appointmentDtoList); // Replace with actual appointment data
        return ResponseEntity.ok(responseDataDto);
    }
    @GetMapping(value = "/staffs", produces = "application/json")
    public ResponseEntity<ResponseDataDto> getStaffs(HttpServletRequest request) {
        ResponseDataDto responseDataDto = new ResponseDataDto();
        List<String> roles = jwtTokenProvider.getRolesFromRequest(request);
        if (roles.isEmpty() || !roles.contains(ContsConfig.MANAGER)) {
            responseDataDto.setErrorMessage("Unauthorized access");
            responseDataDto.setErrorCode("99");
            return ResponseEntity.status(403).body(responseDataDto);
        }
        Optional<List<StaffDto>> staffProfileDtos = staffService.findAll();
        responseDataDto.setData(staffProfileDtos); // Replace with actual staff data
        return ResponseEntity.ok(responseDataDto);
    }
}
