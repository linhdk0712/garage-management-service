package vn.utc.service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import vn.utc.service.dtos.ResponseDataDto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseDataDto> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setErrorCode("400");
        responseDataDto.setErrorMessage("Validation failed");
        responseDataDto.setData(errors);
        
        log.warn("Validation error for request {}: {}", request.getDescription(false), errors);
        return ResponseEntity.badRequest().body(responseDataDto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseDataDto> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setErrorCode("400");
        responseDataDto.setErrorMessage(ex.getMessage());
        
        log.warn("Illegal argument error for request {}: {}", request.getDescription(false), ex.getMessage());
        return ResponseEntity.badRequest().body(responseDataDto);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseDataDto> handleUserNotFoundException(
            UserNotFoundException ex, WebRequest request) {
        
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setErrorCode("404");
        responseDataDto.setErrorMessage(ex.getMessage());
        
        log.warn("User not found for request {}: {}", request.getDescription(false), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDataDto);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseDataDto> handleCustomerNotFoundException(
            CustomerNotFoundException ex, WebRequest request) {
        
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setErrorCode("404");
        responseDataDto.setErrorMessage(ex.getMessage());
        
        log.warn("Customer not found for request {}: {}", request.getDescription(false), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDataDto);
    }

    @ExceptionHandler(VehicleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseDataDto> handleVehicleNotFoundException(
            VehicleNotFoundException ex, WebRequest request) {
        
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setErrorCode("404");
        responseDataDto.setErrorMessage(ex.getMessage());
        
        log.warn("Vehicle not found for request {}: {}", request.getDescription(false), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDataDto);
    }

    @ExceptionHandler(TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ResponseDataDto> handleTokenRefreshException(
            TokenRefreshException ex, WebRequest request) {
        
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setErrorCode("403");
        responseDataDto.setErrorMessage(ex.getMessage());
        
        log.warn("Token refresh error for request {}: {}", request.getDescription(false), ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseDataDto);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ResponseDataDto> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {
        
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setErrorCode("403");
        responseDataDto.setErrorMessage("Access denied: " + ex.getMessage());
        
        log.warn("Access denied for request {}: {}", request.getDescription(false), ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseDataDto);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseDataDto> handleGlobalException(
            Exception ex, WebRequest request) {
        
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setErrorCode("500");
        responseDataDto.setErrorMessage("An unexpected error occurred: " + ex.getMessage());
        
        log.error("Unexpected error for request {}: {}", request.getDescription(false), ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDataDto);
    }
} 