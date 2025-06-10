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
import vn.utc.service.entity.Vehicle;
import vn.utc.service.exception.VehicleNotFoundException;
import vn.utc.service.mapper.AppointmentMapper;
import vn.utc.service.mapper.CustomerMapper;
import vn.utc.service.repo.AppointmentRepository;
import vn.utc.service.repo.VehicleRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final CustomerMapper customerMapper;
    private final VehicleRepository vehicleRepository;

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
        if (status != null && !status.trim().isEmpty() || 
            from != null && !from.trim().isEmpty() || 
            to != null && !to.trim().isEmpty() || 
            date != null && !date.trim().isEmpty()) {
            return appointmentRepository.findByCustomerAndFilters(customerId, status, from, to, date, pageable)
                    .map(appointmentMapper::toDto);
        }
        
        // Fallback to existing method if no filters
        return getAllAppointments(customerId, pageable);
    }
    
    public Page<AppointmentDto> getAllAppointments(Pageable pageable, String status, String from, String to, String date) {
        if (status != null && !status.trim().isEmpty() || 
            from != null && !from.trim().isEmpty() || 
            to != null && !to.trim().isEmpty() || 
            date != null && !date.trim().isEmpty()) {
            return appointmentRepository.findByFilters(status, from, to, date, pageable)
                    .map(appointmentMapper::toDto);
        }
        
        // Fallback to existing method if no filters
        return getAllAppointments(pageable);
    }
}
