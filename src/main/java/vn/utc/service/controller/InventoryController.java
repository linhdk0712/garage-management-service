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
import vn.utc.service.dtos.PaginatedResponseDto;
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
    public ResponseEntity<ResponseDataDto> getAllParts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String stockStatus,
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
            
            Page<SparePart> partsPage = sparePartService.findAll(pageable, category, search, stockStatus);
            PaginatedResponseDto<SparePart> paginatedResponse = PaginatedResponseDto.of(
                partsPage.getContent(),
                partsPage.getNumber(),
                partsPage.getSize(),
                partsPage.getTotalElements()
            );
            
            responseDataDto.setData(paginatedResponse);
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
} 