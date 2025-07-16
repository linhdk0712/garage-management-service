package vn.utc.service.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.utc.service.dtos.ResponseDataDto;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/service-types")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@Tag(name = "Service Types", description = "Service types management")
public class ServiceTypeController {

    /**
     * Get all available service types
     */
    @GetMapping(produces = "application/json")
    public ResponseEntity<ResponseDataDto> getAllServiceTypes() {
        ResponseDataDto responseDataDto = new ResponseDataDto();
        
        List<String> serviceTypes = Arrays.asList(
            "OIL_CHANGE",
            "BRAKE_SERVICE", 
            "TIRE_ROTATION",
            "AIR_FILTER_REPLACEMENT",
            "SPARK_PLUG_REPLACEMENT",
            "TRANSMISSION_SERVICE",
            "COOLANT_FLUSH",
            "POWER_STEERING_FLUID",
            "BRAKE_FLUID_FLUSH",
            "FUEL_FILTER_REPLACEMENT",
            "SERPTINE_BELT_REPLACEMENT",
            "TIMING_BELT_REPLACEMENT",
            "WATER_PUMP_REPLACEMENT",
            "ALTERNATOR_REPLACEMENT",
            "STARTER_REPLACEMENT",
            "BATTERY_REPLACEMENT",
            "CLUTCH_REPLACEMENT",
            "EXHAUST_SYSTEM_REPAIR",
            "SUSPENSION_REPAIR",
            "STEERING_REPAIR",
            "ELECTRICAL_SYSTEM_REPAIR",
            "AC_SYSTEM_REPAIR",
            "HEATING_SYSTEM_REPAIR",
            "DIAGNOSTIC_SERVICE",
            "PRE_PURCHASE_INSPECTION",
            "SAFETY_INSPECTION",
            "EMISSIONS_TEST",
            "WHEEL_ALIGNMENT",
            "WHEEL_BALANCING",
            "TIRE_REPLACEMENT",
            "WINDOW_TINTING",
            "DETAILING_SERVICE",
            "PAINT_PROTECTION",
            "UNDERCOATING",
            "RUST_PROOFING",
            "CUSTOM_MODIFICATIONS",
            "EMERGENCY_REPAIR",
            "ROADSIDE_ASSISTANCE",
            "TOWING_SERVICE"
        );
        
        responseDataDto.setData(serviceTypes);
        return ResponseEntity.ok(responseDataDto);
    }
} 