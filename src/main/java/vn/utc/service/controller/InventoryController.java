package vn.utc.service.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.utc.service.config.ContsConfig;
import vn.utc.service.config.JwtTokenProvider;
import vn.utc.service.dtos.ResponseDataDto;
import vn.utc.service.entity.SparePart;
import vn.utc.service.service.SparePartService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/inventory")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@Tag(name = "Inventory", description = "Inventory management")
public class InventoryController {
    
    private final JwtTokenProvider jwtTokenProvider;
    private final SparePartService sparePartService;
    
    /**
     * Get all spare parts
     */
    @GetMapping(value = "/parts", produces = "application/json")
    public ResponseEntity<ResponseDataDto> getAllParts(HttpServletRequest request) {
        ResponseDataDto responseDataDto = new ResponseDataDto();
        List<String> roles = jwtTokenProvider.getRolesFromRequest(request);
        if (roles.isEmpty() || !roles.contains(ContsConfig.MANAGER)) {
            responseDataDto.setErrorMessage("Unauthorized access");
            responseDataDto.setErrorCode("99");
            return ResponseEntity.status(403).body(responseDataDto);
        }
        
        try {
            List<SparePart> parts = sparePartService.findAll();
            responseDataDto.setData(parts);
            return ResponseEntity.ok(responseDataDto);
        } catch (Exception e) {
            responseDataDto.setErrorMessage("Error fetching parts: " + e.getMessage());
            responseDataDto.setErrorCode("500");
            return ResponseEntity.status(500).body(responseDataDto);
        }
    }
    
    /**
     * Get a specific spare part by ID
     */
    @GetMapping(value = "/parts/{partId}", produces = "application/json")
    public ResponseEntity<ResponseDataDto> getPartById(
            @PathVariable Integer partId,
            HttpServletRequest request) {
        ResponseDataDto responseDataDto = new ResponseDataDto();
        List<String> roles = jwtTokenProvider.getRolesFromRequest(request);
        if (roles.isEmpty() || !roles.contains(ContsConfig.MANAGER)) {
            responseDataDto.setErrorMessage("Unauthorized access");
            responseDataDto.setErrorCode("99");
            return ResponseEntity.status(403).body(responseDataDto);
        }
        
        try {
            Optional<SparePart> part = sparePartService.findById(partId);
            if (part.isPresent()) {
                responseDataDto.setData(part.get());
                return ResponseEntity.ok(responseDataDto);
            } else {
                responseDataDto.setErrorMessage("Part not found");
                responseDataDto.setErrorCode("404");
                return ResponseEntity.status(404).body(responseDataDto);
            }
        } catch (Exception e) {
            responseDataDto.setErrorMessage("Error fetching part: " + e.getMessage());
            responseDataDto.setErrorCode("500");
            return ResponseEntity.status(500).body(responseDataDto);
        }
    }
    
    /**
     * Get low stock items
     */
    @GetMapping(value = "/low-stock", produces = "application/json")
    public ResponseEntity<ResponseDataDto> getLowStockItems(HttpServletRequest request) {
        ResponseDataDto responseDataDto = new ResponseDataDto();
        List<String> roles = jwtTokenProvider.getRolesFromRequest(request);
        if (roles.isEmpty() || !roles.contains(ContsConfig.MANAGER)) {
            responseDataDto.setErrorMessage("Unauthorized access");
            responseDataDto.setErrorCode("99");
            return ResponseEntity.status(403).body(responseDataDto);
        }
        
        try {
            List<SparePart> lowStockItems = sparePartService.findLowStockItems();
            responseDataDto.setData(lowStockItems);
            return ResponseEntity.ok(responseDataDto);
        } catch (Exception e) {
            responseDataDto.setErrorMessage("Error fetching low stock items: " + e.getMessage());
            responseDataDto.setErrorCode("500");
            return ResponseEntity.status(500).body(responseDataDto);
        }
    }
} 