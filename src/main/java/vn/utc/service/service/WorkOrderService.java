package vn.utc.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.utc.service.dtos.WorkOrderDto;
import vn.utc.service.entity.Appointment;
import vn.utc.service.entity.Staff;
import vn.utc.service.entity.WorkOrder;
import vn.utc.service.mapper.WorkOrderMapper;
import vn.utc.service.repo.AppointmentRepository;
import vn.utc.service.repo.StaffRepository;
import vn.utc.service.repo.WorkOrderRepository;

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
public class WorkOrderService {
    private final WorkOrderRepository workOrderRepository;
    private final WorkOrderMapper workOrderMapper;
    private final StaffRepository staffRepository;
    private final AppointmentRepository appointmentRepository;

    public Optional<WorkOrderDto> findById(Integer id) {
        return workOrderRepository.findById(id)
                .map(workOrder -> new WorkOrderDto(
                        workOrder.getId(),
                        workOrder.getAppointment() != null ? workOrder.getAppointment().getId() : null,
                        workOrder.getStaff() != null ? workOrder.getStaff().getId() : null,
                        workOrder.getStartTime(),
                        workOrder.getEndTime(),
                        workOrder.getStatus(),
                        workOrder.getDiagnosticNotes(),
                        workOrder.getTotalCost(),
                        workOrder.getCreatedAt(),
                        workOrder.getUpdatedAt()));
    }

    public List<WorkOrderDto> getWorkOrdersByStaffId(Integer staffId) {
        return workOrderRepository.findByStaffId(staffId).stream()
                .map(workOrder -> new WorkOrderDto(
                        workOrder.getId(),
                        workOrder.getAppointment() != null ? workOrder.getAppointment().getId() : null,
                        workOrder.getStaff() != null ? workOrder.getStaff().getId() : null,
                        workOrder.getStartTime(),
                        workOrder.getEndTime(),
                        workOrder.getStatus(),
                        workOrder.getDiagnosticNotes(),
                        workOrder.getTotalCost(),
                        workOrder.getCreatedAt(),
                        workOrder.getUpdatedAt()))
                .toList();
    }

    public Page<WorkOrderDto> getWorkOrdersByStaffId(Integer staffId, Pageable pageable) {
        return workOrderRepository.findByStaffId(staffId, pageable)
                .map(workOrder -> new WorkOrderDto(
                        workOrder.getId(),
                        workOrder.getAppointment() != null ? workOrder.getAppointment().getId() : null,
                        workOrder.getStaff() != null ? workOrder.getStaff().getId() : null,
                        workOrder.getStartTime(),
                        workOrder.getEndTime(),
                        workOrder.getStatus(),
                        workOrder.getDiagnosticNotes(),
                        workOrder.getTotalCost(),
                        workOrder.getCreatedAt(),
                        workOrder.getUpdatedAt()));
    }

    public Page<WorkOrderDto> getWorkOrdersByStaffId(Integer staffId, Pageable pageable, String status, String from, String to) {
        // Convert String parameters to Instant
        Instant fromInstant = parseDateString(from);
        Instant toInstant = parseDateString(to);
        
        boolean hasStatus = status != null && !status.trim().isEmpty();
        boolean hasDateRange = fromInstant != null && toInstant != null;
        
        if (hasStatus && hasDateRange) {
            return workOrderRepository.findByStaffIdAndStatusAndDateRange(staffId, status, fromInstant, toInstant, pageable)
                    .map(workOrder -> new WorkOrderDto(
                            workOrder.getId(),
                            workOrder.getAppointment() != null ? workOrder.getAppointment().getId() : null,
                            workOrder.getStaff() != null ? workOrder.getStaff().getId() : null,
                            workOrder.getStartTime(),
                            workOrder.getEndTime(),
                            workOrder.getStatus(),
                            workOrder.getDiagnosticNotes(),
                            workOrder.getTotalCost(),
                            workOrder.getCreatedAt(),
                            workOrder.getUpdatedAt()));
        } else if (hasStatus) {
            return workOrderRepository.findByStaffIdAndStatus(staffId, status, pageable)
                    .map(workOrder -> new WorkOrderDto(
                            workOrder.getId(),
                            workOrder.getAppointment() != null ? workOrder.getAppointment().getId() : null,
                            workOrder.getStaff() != null ? workOrder.getStaff().getId() : null,
                            workOrder.getStartTime(),
                            workOrder.getEndTime(),
                            workOrder.getStatus(),
                            workOrder.getDiagnosticNotes(),
                            workOrder.getTotalCost(),
                            workOrder.getCreatedAt(),
                            workOrder.getUpdatedAt()));
        } else if (hasDateRange) {
            return workOrderRepository.findByStaffIdAndDateRange(staffId, fromInstant, toInstant, pageable)
                    .map(workOrder -> new WorkOrderDto(
                            workOrder.getId(),
                            workOrder.getAppointment() != null ? workOrder.getAppointment().getId() : null,
                            workOrder.getStaff() != null ? workOrder.getStaff().getId() : null,
                            workOrder.getStartTime(),
                            workOrder.getEndTime(),
                            workOrder.getStatus(),
                            workOrder.getDiagnosticNotes(),
                            workOrder.getTotalCost(),
                            workOrder.getCreatedAt(),
                            workOrder.getUpdatedAt()));
        } else {
            return workOrderRepository.findByStaffId(staffId, pageable)
                    .map(workOrder -> new WorkOrderDto(
                            workOrder.getId(),
                            workOrder.getAppointment() != null ? workOrder.getAppointment().getId() : null,
                            workOrder.getStaff() != null ? workOrder.getStaff().getId() : null,
                            workOrder.getStartTime(),
                            workOrder.getEndTime(),
                            workOrder.getStatus(),
                            workOrder.getDiagnosticNotes(),
                            workOrder.getTotalCost(),
                            workOrder.getCreatedAt(),
                            workOrder.getUpdatedAt()));
        }
    }

    @Transactional
    public WorkOrderDto createWorkOrder(WorkOrderDto workOrderDto) {
        WorkOrder workOrder = workOrderMapper.toEntity(workOrderDto);
        
        // Set default status if not provided
        if (workOrder.getStatus() == null) {
            workOrder.setStatus("PENDING");
        }
        
        // Set timestamps
        Instant now = Instant.now();
        if (workOrder.getCreatedAt() == null) {
            workOrder.setCreatedAt(now);
        }
        if (workOrder.getUpdatedAt() == null) {
            workOrder.setUpdatedAt(now);
        }
        Staff staff = staffRepository.findStaffById(workOrderDto.staffId())
                .orElseThrow(() -> new RuntimeException("Staff not found with ID: " + workOrderDto.staffId()));

        Appointment appointment = appointmentRepository.findById(workOrderDto.appointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + workOrderDto.appointmentId()));
        workOrder.setStaff(staff);
        workOrder.setAppointment(appointment);
        
        WorkOrder savedWorkOrder = workOrderRepository.save(workOrder);
        return workOrderMapper.toDto(savedWorkOrder);
    }

    @Transactional
    public Optional<WorkOrderDto> updateWorkOrder(Integer id, WorkOrderDto workOrderDto) {
        return workOrderRepository.findById(id)
                .map(existingWorkOrder -> {
                    workOrderMapper.partialUpdate(workOrderDto, existingWorkOrder);
                    existingWorkOrder.setUpdatedAt(Instant.now());
                    WorkOrder savedWorkOrder = workOrderRepository.save(existingWorkOrder);
                    return workOrderMapper.toDto(savedWorkOrder);
                });
    }

    @Transactional
    public boolean deleteWorkOrder(Integer id) {
        if (workOrderRepository.existsById(id)) {
            workOrderRepository.deleteById(id);
            return true;
        }
        return false;
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
} 