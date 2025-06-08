package vn.utc.service.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import vn.utc.service.config.ContsConfig;
import vn.utc.service.config.JwtTokenProvider;
import vn.utc.service.dtos.*;
import vn.utc.service.service.RoleService;
import vn.utc.service.service.StaffService;
import vn.utc.service.service.UserService;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/staff")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@Tag(name = "Staff", description = "Staff information management")
public class StaffController {
    private final JwtTokenProvider jwtTokenProvider;
    private  final StaffService staffService;


    @PostMapping(produces = "application/json",consumes = "application/json")
    public ResponseEntity<ResponseDataDto> create(@RequestBody StaffRequest registerRequest, HttpServletRequest request) {
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
}
