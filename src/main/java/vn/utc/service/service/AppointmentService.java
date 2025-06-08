package vn.utc.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.utc.service.dtos.AppointmentDto;
import vn.utc.service.dtos.CustomerDto;
import vn.utc.service.entity.Appointment;
import vn.utc.service.mapper.AppointmentMapper;
import vn.utc.service.mapper.CustomerMapper;
import vn.utc.service.repo.AppointmentRepository;

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

    public AppointmentDto createAppointment(AppointmentDto appointmentDto, CustomerDto customerDto) {
        Appointment appointment;
        appointment = appointmentMapper.toEntity(appointmentDto);
        appointment.setCustomer(customerMapper.toEntity(customerDto));
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(savedAppointment);
    }
    public List<AppointmentDto> getAllAppointments(Integer customerId) {
        return appointmentRepository.findAll().stream()
                .filter(appointment -> appointment.getCustomer() != null && appointment.getCustomer().getId().equals(customerId))
                .map(appointmentMapper::toDto)
                .toList();
    }
    public List<AppointmentDto> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(appointmentMapper::toDto)
                .toList();
    }


}
