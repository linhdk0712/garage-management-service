package vn.utc.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.utc.service.dtos.AppointmentDto;
import vn.utc.service.dtos.CustomerDto;
import vn.utc.service.entity.Appointment;
import vn.utc.service.mapper.AppointmentMapper;
import vn.utc.service.mapper.CustomerMapper;
import vn.utc.service.repo.AppointmentRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final CustomerMapper customerMapper;

    public Optional<AppointmentDto> findById(Integer id) {
        return appointmentRepository.findById(id)
                .map(appointment ->
                        new AppointmentDto(appointment.getId(),
                                appointment.getAppointmentDate(),
                                appointment.getEstimatedCompletion(),
                                appointment.getStatus(),
                                appointment.getServiceType(),
                                appointment.getDescription(),
                                appointment.getCreatedAt(),
                                appointment.getUpdatedAt()));
    }

    @Transactional
    public AppointmentDto createAppointment(AppointmentDto appointmentDto, CustomerDto customerDto) {
        Appointment appointment = appointmentMapper.toEntity(appointmentDto);
        appointment.setCustomer(customerMapper.toEntity(customerDto));
        
        // Set default status if not provided
        if (appointment.getStatus() == null) {
            appointment.setStatus("PENDING");
        }
        
        // Set timestamps
        Instant now = Instant.now();
        if (appointment.getCreatedAt() == null) {
            appointment.setCreatedAt(now);
        }
        if (appointment.getUpdatedAt() == null) {
            appointment.setUpdatedAt(now);
        }
        
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(savedAppointment);
    }
    
    public List<AppointmentDto> getAllAppointments(Integer customerId) {
        return appointmentRepository.findAll().stream()
                .filter(appointment -> appointment.getCustomer() != null && appointment.getCustomer().getId().equals(customerId))
                .map(appointmentMapper::toDto)
                .toList();
    }
    
    public Page<AppointmentDto> getAllAppointments(Integer customerId, Pageable pageable) {
        List<Appointment> allAppointments = appointmentRepository.findAll();
        List<Appointment> customerAppointments = allAppointments.stream()
                .filter(appointment -> appointment.getCustomer() != null && appointment.getCustomer().getId().equals(customerId))
                .toList();
        
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), customerAppointments.size());
        
        if (start > customerAppointments.size()) {
            return new PageImpl<>(List.of(), pageable, customerAppointments.size());
        }
        
        List<AppointmentDto> appointmentDtos = customerAppointments.subList(start, end).stream()
                .map(appointmentMapper::toDto)
                .toList();
        
        return new PageImpl<>(appointmentDtos, pageable, customerAppointments.size());
    }
    
    public List<AppointmentDto> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(appointmentMapper::toDto)
                .toList();
    }
    
    public Page<AppointmentDto> getAllAppointments(Pageable pageable) {
        return appointmentRepository.findAll(pageable)
                .map(appointmentMapper::toDto);
    }

    public Page<AppointmentDto> getAllAppointments(Integer customerId, Pageable pageable, String status, String from, String to, String date) {
        // Convert String parameters to Instant
        Instant fromInstant = parseDateString(from);
        Instant toInstant = parseDateString(to);
        
        boolean hasStatus = status != null && !status.trim().isEmpty();
        boolean hasDateRange = fromInstant != null && toInstant != null;
        
        if (hasStatus && hasDateRange) {
            return appointmentRepository.findByCustomerIdAndStatusAndDateRange(customerId, status, fromInstant, toInstant, pageable)
                    .map(appointmentMapper::toDto);
        } else if (hasStatus) {
            return appointmentRepository.findByCustomerIdAndStatus(customerId, status, pageable)
                    .map(appointmentMapper::toDto);
        } else if (hasDateRange) {
            return appointmentRepository.findByCustomerIdAndDateRange(customerId, fromInstant, toInstant, pageable)
                    .map(appointmentMapper::toDto);
        } else {
            return appointmentRepository.findByCustomerId(customerId, pageable)
                    .map(appointmentMapper::toDto);
        }
    }
    
    public Page<AppointmentDto> getAllAppointments(Pageable pageable, String status, String from, String to, String date) {
        // Convert String parameters to Instant
        Instant fromInstant = parseDateString(from);
        Instant toInstant = parseDateString(to);
        
        boolean hasStatus = status != null && !status.trim().isEmpty();
        boolean hasDateRange = fromInstant != null && toInstant != null;
        
        if (hasStatus && hasDateRange) {
            return appointmentRepository.findByStatusAndDateRange(status, fromInstant, toInstant, pageable)
                    .map(appointmentMapper::toDto);
        } else if (hasStatus) {
            return appointmentRepository.findByStatus(status, pageable)
                    .map(appointmentMapper::toDto);
        } else if (hasDateRange) {
            return appointmentRepository.findByDateRange(fromInstant, toInstant, pageable)
                    .map(appointmentMapper::toDto);
        } else {
            return appointmentRepository.findAll(pageable)
                    .map(appointmentMapper::toDto);
        }
    }
    
    /**
     * Parse a date string to Instant. Supports multiple formats:
     * - ISO-8601 format (2023-12-25T10:30:00Z)
     * - Date only format (2023-12-25)
     * - Date time format (2023-12-25T10:30:00)
     */
    private Instant parseDateString(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        
        try {
            // Try parsing as ISO-8601 Instant
            return Instant.parse(dateString);
        } catch (DateTimeParseException e1) {
            try {
                // Try parsing as LocalDateTime
                LocalDateTime localDateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
            } catch (DateTimeParseException e2) {
                try {
                    // Try parsing as LocalDate (assume start of day)
                    LocalDate localDate = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
                    return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
                } catch (DateTimeParseException e3) {
                    // If all parsing fails, return null
                    return null;
                }
            }
        }
    }

    /**
     * Save multiple appointments for data initialization purposes
     * @param appointments List of Appointment entities to save
     * @return List of saved Appointment entities
     */
    @Transactional
    public List<Appointment> saveAllForInitialization(List<Appointment> appointments) {
        return appointmentRepository.saveAll(appointments);
    }
}
