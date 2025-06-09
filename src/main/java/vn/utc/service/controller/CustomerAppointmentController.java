package vn.utc.service.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.utc.service.config.JwtTokenProvider;
import vn.utc.service.dtos.AppointmentDto;
import vn.utc.service.dtos.ResponseDataDto;
import vn.utc.service.service.AppointmentService;
import vn.utc.service.service.CustomerService;

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


    @GetMapping(produces = "application/json")
    public ResponseEntity<ResponseDataDto> getAllAppointments(HttpServletRequest request) {
        ResponseDataDto responseDataDto = new ResponseDataDto();
        String userName = jwtTokenProvider.getUsernameFromRequest(request);
        Integer customerId = customerService.finByUserName(userName)
                .map(customerDto -> customerDto.id())
                .orElseThrow(() -> new RuntimeException("Customer not found for user: " + userName));
        List<AppointmentDto> appointmentDtos = appointmentService.getAllAppointments(customerId);
        responseDataDto.setData(appointmentDtos);
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
        appointmentService.createAppointment(appointmentDto, customerService.finByUserName(userName)
                .orElseThrow(() -> new RuntimeException("Customer not found for user: " + userName)));
        responseDataDto.setData(appointmentDto);
        return ResponseEntity.ok(responseDataDto);
    }
}
