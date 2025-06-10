package vn.utc.service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import vn.utc.service.dtos.AppointmentDto;
import vn.utc.service.dtos.CustomerDto;
import vn.utc.service.entity.Appointment;
import vn.utc.service.entity.Customer;
import vn.utc.service.entity.Vehicle;
import vn.utc.service.mapper.AppointmentMapper;
import vn.utc.service.mapper.CustomerMapper;
import vn.utc.service.repo.AppointmentRepository;
import vn.utc.service.repo.VehicleRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AppointmentService Unit Tests")
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AppointmentMapper appointmentMapper;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    private Appointment appointment;
    private AppointmentDto appointmentDto;
    private Customer customer;
    private CustomerDto customerDto;
    private Instant now;

    @BeforeEach
    void setUp() {
        now = Instant.now();
        
        customer = new Customer()
                .setId(1)
                .setFirstName("John")
                .setLastName("Doe");

        customerDto = new CustomerDto(1, "John", "Doe", "123 Test St", "Test City", "Test State", null, null, null);

        appointment = new Appointment();
        appointment.setId(1);
        appointment.setAppointmentDate(now.plusSeconds(3600)); // 1 hour from now
        appointment.setEstimatedCompletion(now.plusSeconds(7200)); // 2 hours from now
        appointment.setStatus("PENDING");
        appointment.setServiceType("OIL_CHANGE");
        appointment.setDescription("Regular oil change service");
        appointment.setCreatedAt(now);
        appointment.setUpdatedAt(now);
        appointment.setCustomer(customer);

        appointmentDto = new AppointmentDto(
                1,
                appointment.getAppointmentDate(),
                appointment.getEstimatedCompletion(),
                appointment.getStatus(),
                appointment.getServiceType(),
                appointment.getDescription(),
                appointment.getCreatedAt(),
                appointment.getUpdatedAt()
        );
    }

    @Test
    @DisplayName("Should find appointment by ID when appointment exists")
    void findById_WhenAppointmentExists_ShouldReturnAppointmentDto() {
        // Given
        when(appointmentRepository.findById(1)).thenReturn(Optional.of(appointment));

        // When
        Optional<AppointmentDto> result = appointmentService.findById(1);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(1);
        assertThat(result.get().appointmentDate()).isEqualTo(appointment.getAppointmentDate());
        assertThat(result.get().status()).isEqualTo("PENDING");
        verify(appointmentRepository).findById(1);
    }

    @Test
    @DisplayName("Should return empty when appointment not found by ID")
    void findById_WhenAppointmentNotFound_ShouldReturnEmpty() {
        // Given
        when(appointmentRepository.findById(999)).thenReturn(Optional.empty());

        // When
        Optional<AppointmentDto> result = appointmentService.findById(999);

        // Then
        assertThat(result).isEmpty();
        verify(appointmentRepository).findById(999);
    }

    @Test
    @DisplayName("Should create appointment successfully")
    void createAppointment_ShouldCreateAndReturnAppointmentDto() {
        // Given
        AppointmentDto inputDto = new AppointmentDto(
                null,
                now.plusSeconds(3600),
                now.plusSeconds(7200),
                null, // status will be set to default
                "OIL_CHANGE",
                "Regular oil change service",
                null, // createdAt will be set
                null  // updatedAt will be set
        );

        Appointment mappedAppointment = new Appointment();
        mappedAppointment.setAppointmentDate(inputDto.appointmentDate());
        mappedAppointment.setEstimatedCompletion(inputDto.estimatedCompletion());
        mappedAppointment.setServiceType(inputDto.serviceType());
        mappedAppointment.setDescription(inputDto.description());

        Appointment savedAppointment = new Appointment();
        savedAppointment.setId(1);
        savedAppointment.setAppointmentDate(inputDto.appointmentDate());
        savedAppointment.setEstimatedCompletion(inputDto.estimatedCompletion());
        savedAppointment.setStatus("PENDING");
        savedAppointment.setServiceType(inputDto.serviceType());
        savedAppointment.setDescription(inputDto.description());
        savedAppointment.setCreatedAt(now);
        savedAppointment.setUpdatedAt(now);
        savedAppointment.setCustomer(customer);

        AppointmentDto expectedDto = new AppointmentDto(
                1,
                inputDto.appointmentDate(),
                inputDto.estimatedCompletion(),
                "PENDING",
                inputDto.serviceType(),
                inputDto.description(),
                now,
                now
        );

        when(appointmentMapper.toEntity(inputDto)).thenReturn(mappedAppointment);
        when(customerMapper.toEntity(customerDto)).thenReturn(customer);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(savedAppointment);
        when(appointmentMapper.toDto(savedAppointment)).thenReturn(expectedDto);

        // When
        AppointmentDto result = appointmentService.createAppointment(inputDto, customerDto);

        // Then
        assertThat(result).isEqualTo(expectedDto);
        assertThat(result.status()).isEqualTo("PENDING");
        verify(appointmentMapper).toEntity(inputDto);
        verify(customerMapper).toEntity(customerDto);
        verify(appointmentRepository).save(any(Appointment.class));
        verify(appointmentMapper).toDto(savedAppointment);
    }

    @Test
    @DisplayName("Should create appointment with existing status")
    void createAppointment_WithExistingStatus_ShouldPreserveStatus() {
        // Given
        AppointmentDto inputDto = new AppointmentDto(
                null,
                now.plusSeconds(3600),
                now.plusSeconds(7200),
                "CONFIRMED", // existing status
                "OIL_CHANGE",
                "Regular oil change service",
                now,
                now
        );

        Appointment mappedAppointment = new Appointment();
        mappedAppointment.setStatus("CONFIRMED");
        mappedAppointment.setCreatedAt(now);
        mappedAppointment.setUpdatedAt(now);

        Appointment savedAppointment = new Appointment();
        savedAppointment.setId(1);
        savedAppointment.setStatus("CONFIRMED");
        savedAppointment.setCreatedAt(now);
        savedAppointment.setUpdatedAt(now);

        AppointmentDto expectedDto = new AppointmentDto(
                1,
                inputDto.appointmentDate(),
                inputDto.estimatedCompletion(),
                "CONFIRMED",
                inputDto.serviceType(),
                inputDto.description(),
                now,
                now
        );

        when(appointmentMapper.toEntity(inputDto)).thenReturn(mappedAppointment);
        when(customerMapper.toEntity(customerDto)).thenReturn(customer);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(savedAppointment);
        when(appointmentMapper.toDto(savedAppointment)).thenReturn(expectedDto);

        // When
        AppointmentDto result = appointmentService.createAppointment(inputDto, customerDto);

        // Then
        assertThat(result.status()).isEqualTo("CONFIRMED");
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    @DisplayName("Should return all appointments for customer")
    void getAllAppointments_WithCustomerId_ShouldReturnCustomerAppointments() {
        // Given
        List<Appointment> allAppointments = List.of(appointment);
        List<AppointmentDto> expectedDtos = List.of(appointmentDto);

        when(appointmentRepository.findAll()).thenReturn(allAppointments);
        when(appointmentMapper.toDto(appointment)).thenReturn(appointmentDto);

        // When
        List<AppointmentDto> result = appointmentService.getAllAppointments(1);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(expectedDtos);
        verify(appointmentRepository).findAll();
        verify(appointmentMapper).toDto(appointment);
    }

    @Test
    @DisplayName("Should return empty list when no appointments for customer")
    void getAllAppointments_WithCustomerId_WhenNoAppointments_ShouldReturnEmptyList() {
        // Given
        Appointment otherCustomerAppointment = new Appointment();
        otherCustomerAppointment.setId(2);
        Customer otherCustomer = new Customer().setId(2);
        otherCustomerAppointment.setCustomer(otherCustomer);

        when(appointmentRepository.findAll()).thenReturn(List.of(otherCustomerAppointment));

        // When
        List<AppointmentDto> result = appointmentService.getAllAppointments(1);

        // Then
        assertThat(result).isEmpty();
        verify(appointmentRepository).findAll();
        verifyNoInteractions(appointmentMapper);
    }

    @Test
    @DisplayName("Should return paginated appointments for customer")
    void getAllAppointments_WithCustomerIdAndPageable_ShouldReturnPaginatedAppointments() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Appointment> allAppointments = List.of(appointment);
        Page<AppointmentDto> expectedPage = new PageImpl<>(List.of(appointmentDto), pageable, 1);

        when(appointmentRepository.findAll()).thenReturn(allAppointments);
        when(appointmentMapper.toDto(appointment)).thenReturn(appointmentDto);

        // When
        Page<AppointmentDto> result = appointmentService.getAllAppointments(1, pageable);

        // Then
        assertThat(result).isEqualTo(expectedPage);
        assertThat(result.getContent()).hasSize(1);
        verify(appointmentRepository).findAll();
        verify(appointmentMapper).toDto(appointment);
    }

    @Test
    @DisplayName("Should return all appointments")
    void getAllAppointments_WithoutCustomerId_ShouldReturnAllAppointments() {
        // Given
        List<Appointment> allAppointments = List.of(appointment);
        List<AppointmentDto> expectedDtos = List.of(appointmentDto);

        when(appointmentRepository.findAll()).thenReturn(allAppointments);
        when(appointmentMapper.toDto(appointment)).thenReturn(appointmentDto);

        // When
        List<AppointmentDto> result = appointmentService.getAllAppointments();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(expectedDtos);
        verify(appointmentRepository).findAll();
        verify(appointmentMapper).toDto(appointment);
    }

    @Test
    @DisplayName("Should return paginated appointments")
    void getAllAppointments_WithPageable_ShouldReturnPaginatedAppointments() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Appointment> appointmentPage = new PageImpl<>(List.of(appointment), pageable, 1);
        Page<AppointmentDto> expectedPage = new PageImpl<>(List.of(appointmentDto), pageable, 1);

        when(appointmentRepository.findAll(pageable)).thenReturn(appointmentPage);
        when(appointmentMapper.toDto(appointment)).thenReturn(appointmentDto);

        // When
        Page<AppointmentDto> result = appointmentService.getAllAppointments(pageable);

        // Then
        assertThat(result).isEqualTo(expectedPage);
        assertThat(result.getContent()).hasSize(1);
        verify(appointmentRepository).findAll(pageable);
        verify(appointmentMapper).toDto(appointment);
    }

    @Test
    @DisplayName("Should return filtered appointments for customer when filters provided")
    void getAllAppointments_WithCustomerIdAndFilters_ShouldReturnFilteredAppointments() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        String status = "PENDING";
        String from = "2024-01-01";
        String to = "2024-12-31";
        String date = "2024-06-15";
        
        Page<Appointment> appointmentPage = new PageImpl<>(List.of(appointment), pageable, 1);
        Page<AppointmentDto> expectedPage = new PageImpl<>(List.of(appointmentDto), pageable, 1);

        when(appointmentRepository.findByCustomerAndFilters(1, status, from, to, date, pageable))
                .thenReturn(appointmentPage);
        when(appointmentMapper.toDto(appointment)).thenReturn(appointmentDto);

        // When
        Page<AppointmentDto> result = appointmentService.getAllAppointments(1, pageable, status, from, to, date);

        // Then
        assertThat(result).isEqualTo(expectedPage);
        assertThat(result.getContent()).hasSize(1);
        verify(appointmentRepository).findByCustomerAndFilters(1, status, from, to, date, pageable);
        verify(appointmentMapper).toDto(appointment);
    }

    @Test
    @DisplayName("Should return all appointments for customer when no filters provided")
    void getAllAppointments_WithCustomerIdAndNoFilters_ShouldReturnAllCustomerAppointments() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Appointment> allAppointments = List.of(appointment);
        Page<AppointmentDto> expectedPage = new PageImpl<>(List.of(appointmentDto), pageable, 1);

        when(appointmentRepository.findAll()).thenReturn(allAppointments);
        when(appointmentMapper.toDto(appointment)).thenReturn(appointmentDto);

        // When
        Page<AppointmentDto> result = appointmentService.getAllAppointments(1, pageable, null, null, null, null);

        // Then
        assertThat(result).isEqualTo(expectedPage);
        assertThat(result.getContent()).hasSize(1);
        verify(appointmentRepository).findAll();
        verify(appointmentMapper).toDto(appointment);
    }

    @Test
    @DisplayName("Should return filtered appointments when filters provided")
    void getAllAppointments_WithFilters_ShouldReturnFilteredAppointments() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        String status = "PENDING";
        String from = "2024-01-01";
        String to = "2024-12-31";
        String date = "2024-06-15";
        
        Page<Appointment> appointmentPage = new PageImpl<>(List.of(appointment), pageable, 1);
        Page<AppointmentDto> expectedPage = new PageImpl<>(List.of(appointmentDto), pageable, 1);

        when(appointmentRepository.findByFilters(status, from, to, date, pageable))
                .thenReturn(appointmentPage);
        when(appointmentMapper.toDto(appointment)).thenReturn(appointmentDto);

        // When
        Page<AppointmentDto> result = appointmentService.getAllAppointments(pageable, status, from, to, date);

        // Then
        assertThat(result).isEqualTo(expectedPage);
        assertThat(result.getContent()).hasSize(1);
        verify(appointmentRepository).findByFilters(status, from, to, date, pageable);
        verify(appointmentMapper).toDto(appointment);
    }

    @Test
    @DisplayName("Should return all appointments when no filters provided")
    void getAllAppointments_WithNoFilters_ShouldReturnAllAppointments() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Appointment> appointmentPage = new PageImpl<>(List.of(appointment), pageable, 1);
        Page<AppointmentDto> expectedPage = new PageImpl<>(List.of(appointmentDto), pageable, 1);

        when(appointmentRepository.findAll(pageable)).thenReturn(appointmentPage);
        when(appointmentMapper.toDto(appointment)).thenReturn(appointmentDto);

        // When
        Page<AppointmentDto> result = appointmentService.getAllAppointments(pageable, null, null, null, null);

        // Then
        assertThat(result).isEqualTo(expectedPage);
        assertThat(result.getContent()).hasSize(1);
        verify(appointmentRepository).findAll(pageable);
        verify(appointmentMapper).toDto(appointment);
    }
} 