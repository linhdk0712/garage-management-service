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
import vn.utc.service.dtos.CustomerDto;
import vn.utc.service.dtos.UserDto;
import vn.utc.service.dtos.VehicleDto;
import vn.utc.service.entity.Customer;
import vn.utc.service.entity.User;
import vn.utc.service.entity.Vehicle;
import vn.utc.service.exception.CustomerNotFoundException;
import vn.utc.service.exception.UserNotFoundException;
import vn.utc.service.exception.VehicleNotFoundException;
import vn.utc.service.mapper.CustomerMapper;
import vn.utc.service.mapper.VehicleMapper;
import vn.utc.service.repo.VehicleRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("VehicleService Unit Tests")
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleMapper vehicleMapper;

    @Mock
    private CustomerService customerService;

    @Mock
    private UserService userService;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private VehicleService vehicleService;

    private Vehicle vehicle;
    private VehicleDto vehicleDto;
    private User user;
    private UserDto userDto;
    private Customer customer;
    private CustomerDto customerDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        userDto = new UserDto();
        userDto.setId(1);
        userDto.setUsername("testuser");
        userDto.setEmail("test@example.com");

        customer = new Customer()
                .setId(1)
                .setUser(user)
                .setFirstName("John")
                .setLastName("Doe");

        customerDto = new CustomerDto(1, "John", "Doe", "123 Test St", "Test City", "Test State", null, null, null);

        vehicle = new Vehicle();
        vehicle.setId(1);
        vehicle.setMake("Toyota");
        vehicle.setModel("Camry");
        vehicle.setYear(2020);
        vehicle.setLicensePlate("ABC123");
        vehicle.setVin("1HGBH41JXMN109186");
        vehicle.setColor("Blue");
        vehicle.setMileage(50000);
        vehicle.setLastServiceDate(LocalDate.of(2023, 12, 1));
        vehicle.setRegistrationDate(Instant.now());
        vehicle.setCustomer(customer);

        vehicleDto = new VehicleDto(
                1,
                "Toyota",
                "Camry",
                2020,
                "ABC123",
                "1HGBH41JXMN109186",
                "Blue",
                50000,
                LocalDate.of(2023, 12, 1),
                Instant.now()
        );
    }

    @Test
    @DisplayName("Should return all vehicles")
    void getAllVehicles_ShouldReturnAllVehicles() {
        // Given
        List<Vehicle> vehicles = List.of(vehicle);
        List<VehicleDto> expectedDtos = List.of(vehicleDto);

        when(vehicleRepository.findAll()).thenReturn(vehicles);
        when(vehicleMapper.toDto(vehicle)).thenReturn(vehicleDto);

        // When
        List<VehicleDto> result = vehicleService.getAllVehicles();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(expectedDtos);
        verify(vehicleRepository).findAll();
        verify(vehicleMapper).toDto(vehicle);
    }

    @Test
    @DisplayName("Should return empty list when no vehicles exist")
    void getAllVehicles_WhenNoVehicles_ShouldReturnEmptyList() {
        // Given
        when(vehicleRepository.findAll()).thenReturn(List.of());

        // When
        List<VehicleDto> result = vehicleService.getAllVehicles();

        // Then
        assertThat(result).isEmpty();
        verify(vehicleRepository).findAll();
        verifyNoInteractions(vehicleMapper);
    }

    @Test
    @DisplayName("Should return paginated vehicles")
    void getAllVehicles_WithPageable_ShouldReturnPaginatedVehicles() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Vehicle> vehiclePage = new PageImpl<>(List.of(vehicle), pageable, 1);
        Page<VehicleDto> expectedPage = new PageImpl<>(List.of(vehicleDto), pageable, 1);

        when(vehicleRepository.findAll(pageable)).thenReturn(vehiclePage);
        when(vehicleMapper.toDto(vehicle)).thenReturn(vehicleDto);

        // When
        Page<VehicleDto> result = vehicleService.getAllVehicles(pageable);

        // Then
        assertThat(result).isEqualTo(expectedPage);
        assertThat(result.getContent()).hasSize(1);
        verify(vehicleRepository).findAll(pageable);
        verify(vehicleMapper).toDto(vehicle);
    }

    @Test
    @DisplayName("Should return filtered vehicles when filters provided")
    void getAllVehicles_WithFilters_ShouldReturnFilteredVehicles() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        String search = "Toyota";
        String make = "Toyota";
        String model = "Camry";
        Integer year = 2020;
        Integer customerId = 1;

        Page<Vehicle> vehiclePage = new PageImpl<>(List.of(vehicle), pageable, 1);
        Page<VehicleDto> expectedPage = new PageImpl<>(List.of(vehicleDto), pageable, 1);

        when(vehicleRepository.findByFilters(search, make, model, year, customerId, pageable))
                .thenReturn(vehiclePage);
        when(vehicleMapper.toDto(vehicle)).thenReturn(vehicleDto);

        // When
        Page<VehicleDto> result = vehicleService.getAllVehicles(pageable, search, make, model, year, customerId);

        // Then
        assertThat(result).isEqualTo(expectedPage);
        assertThat(result.getContent()).hasSize(1);
        verify(vehicleRepository).findByFilters(search, make, model, year, customerId, pageable);
        verify(vehicleMapper).toDto(vehicle);
    }

    @Test
    @DisplayName("Should return all vehicles when no filters provided")
    void getAllVehicles_WithNoFilters_ShouldReturnAllVehicles() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Vehicle> vehiclePage = new PageImpl<>(List.of(vehicle), pageable, 1);
        Page<VehicleDto> expectedPage = new PageImpl<>(List.of(vehicleDto), pageable, 1);

        when(vehicleRepository.findAll(pageable)).thenReturn(vehiclePage);
        when(vehicleMapper.toDto(vehicle)).thenReturn(vehicleDto);

        // When
        Page<VehicleDto> result = vehicleService.getAllVehicles(pageable, null, null, null, null, null);

        // Then
        assertThat(result).isEqualTo(expectedPage);
        assertThat(result.getContent()).hasSize(1);
        verify(vehicleRepository).findAll(pageable);
        verify(vehicleMapper).toDto(vehicle);
    }

    @Test
    @DisplayName("Should return vehicles by customer ID")
    void getVehiclesByCustomerId_ShouldReturnCustomerVehicles() {
        // Given
        List<Vehicle> vehicles = List.of(vehicle);
        List<VehicleDto> expectedDtos = List.of(vehicleDto);

        when(vehicleRepository.findVehiclesByCustomerId(1)).thenReturn(vehicles);
        when(vehicleMapper.toDto(vehicle)).thenReturn(vehicleDto);

        // When
        List<VehicleDto> result = vehicleService.getVehiclesByCustomerId(1);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(expectedDtos);
        verify(vehicleRepository).findVehiclesByCustomerId(1);
        verify(vehicleMapper).toDto(vehicle);
    }

    @Test
    @DisplayName("Should return empty list when no vehicles for customer")
    void getVehiclesByCustomerId_WhenNoVehicles_ShouldReturnEmptyList() {
        // Given
        when(vehicleRepository.findVehiclesByCustomerId(999)).thenReturn(List.of());

        // When
        List<VehicleDto> result = vehicleService.getVehiclesByCustomerId(999);

        // Then
        assertThat(result).isEmpty();
        verify(vehicleRepository).findVehiclesByCustomerId(999);
        verifyNoInteractions(vehicleMapper);
    }

    @Test
    @DisplayName("Should return paginated vehicles by customer ID")
    void getVehiclesByCustomerId_WithPageable_ShouldReturnPaginatedVehicles() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Vehicle> vehiclePage = new PageImpl<>(List.of(vehicle), pageable, 1);
        Page<VehicleDto> expectedPage = new PageImpl<>(List.of(vehicleDto), pageable, 1);

        when(vehicleRepository.findVehiclesByCustomerId(1, pageable)).thenReturn(vehiclePage);
        when(vehicleMapper.toDto(vehicle)).thenReturn(vehicleDto);

        // When
        Page<VehicleDto> result = vehicleService.getVehiclesByCustomerId(1, pageable);

        // Then
        assertThat(result).isEqualTo(expectedPage);
        assertThat(result.getContent()).hasSize(1);
        verify(vehicleRepository).findVehiclesByCustomerId(1, pageable);
        verify(vehicleMapper).toDto(vehicle);
    }

    @Test
    @DisplayName("Should return vehicle by ID when exists")
    void getVehicleById_WhenVehicleExists_ShouldReturnVehicle() {
        // Given
        when(vehicleRepository.findById(1)).thenReturn(Optional.of(vehicle));
        when(vehicleMapper.toDto(vehicle)).thenReturn(vehicleDto);

        // When
        Optional<VehicleDto> result = vehicleService.getVehicleById(1);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(vehicleDto);
        verify(vehicleRepository).findById(1);
        verify(vehicleMapper).toDto(vehicle);
    }

    @Test
    @DisplayName("Should return empty when vehicle not found by ID")
    void getVehicleById_WhenVehicleNotFound_ShouldReturnEmpty() {
        // Given
        when(vehicleRepository.findById(999)).thenReturn(Optional.empty());

        // When
        Optional<VehicleDto> result = vehicleService.getVehicleById(999);

        // Then
        assertThat(result).isEmpty();
        verify(vehicleRepository).findById(999);
        verifyNoInteractions(vehicleMapper);
    }

    @Test
    @DisplayName("Should create vehicle successfully")
    void createVehicle_ShouldCreateAndReturnVehicle() {
        // Given
        VehicleDto newVehicleDto = new VehicleDto(
                null,
                "Honda",
                "Civic",
                2021,
                "XYZ789",
                "2HGBH41JXMN109187",
                "Red",
                30000,
                LocalDate.of(2024, 1, 1),
                Instant.now()
        );

        Vehicle newVehicle = new Vehicle();
        newVehicle.setMake("Honda");
        newVehicle.setModel("Civic");
        newVehicle.setYear(2021);
        newVehicle.setLicensePlate("XYZ789");
        newVehicle.setCustomer(customer);

        Vehicle savedVehicle = new Vehicle();
        savedVehicle.setId(2);
        savedVehicle.setMake("Honda");
        savedVehicle.setModel("Civic");
        savedVehicle.setYear(2021);
        savedVehicle.setLicensePlate("XYZ789");
        savedVehicle.setCustomer(customer);

        VehicleDto savedVehicleDto = new VehicleDto(
                2,
                "Honda",
                "Civic",
                2021,
                "XYZ789",
                "2HGBH41JXMN109187",
                "Red",
                30000,
                LocalDate.of(2024, 1, 1),
                Instant.now()
        );

        when(vehicleRepository.existsByLicensePlate("XYZ789")).thenReturn(false);
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(userDto));
        when(customerService.findByCustomerId(1)).thenReturn(Optional.of(customerDto));
        when(vehicleMapper.toEntity(newVehicleDto)).thenReturn(newVehicle);
        when(customerMapper.toEntity(customerDto)).thenReturn(customer);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(savedVehicle);
        when(vehicleMapper.toDto(savedVehicle)).thenReturn(savedVehicleDto);

        // When
        VehicleDto result = vehicleService.createVehicle(newVehicleDto, "testuser");

        // Then
        assertThat(result).isEqualTo(savedVehicleDto);
        assertThat(result.vehicleId()).isEqualTo(2);
        verify(vehicleRepository).existsByLicensePlate("XYZ789");
        verify(userService).findByUsername("testuser");
        verify(customerService).findByCustomerId(1);
        verify(vehicleMapper).toEntity(newVehicleDto);
        verify(customerMapper).toEntity(customerDto);
        verify(vehicleRepository).save(any(Vehicle.class));
        verify(vehicleMapper).toDto(savedVehicle);
    }

    @Test
    @DisplayName("Should throw exception when license plate already exists")
    void createVehicle_WhenLicensePlateExists_ShouldThrowException() {
        // Given
        VehicleDto newVehicleDto = new VehicleDto(
                null,
                "Honda",
                "Civic",
                2021,
                "ABC123", // Existing license plate
                "2HGBH41JXMN109187",
                "Red",
                30000,
                LocalDate.of(2024, 1, 1),
                Instant.now()
        );

        when(vehicleRepository.existsByLicensePlate("ABC123")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> vehicleService.createVehicle(newVehicleDto, "testuser"))
                .isInstanceOf(VehicleNotFoundException.class)
                .hasMessage("Vehicle with license plate ABC123 already exists");
        verify(vehicleRepository).existsByLicensePlate("ABC123");
        verifyNoInteractions(userService, customerService, vehicleMapper, customerMapper);
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void createVehicle_WhenUserNotFound_ShouldThrowException() {
        // Given
        VehicleDto newVehicleDto = new VehicleDto(
                null,
                "Honda",
                "Civic",
                2021,
                "XYZ789",
                "2HGBH41JXMN109187",
                "Red",
                30000,
                LocalDate.of(2024, 1, 1),
                Instant.now()
        );

        when(vehicleRepository.existsByLicensePlate("XYZ789")).thenReturn(false);
        when(userService.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> vehicleService.createVehicle(newVehicleDto, "nonexistent"))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found");
        verify(vehicleRepository).existsByLicensePlate("XYZ789");
        verify(userService).findByUsername("nonexistent");
        verifyNoInteractions(customerService, vehicleMapper, customerMapper);
    }

    @Test
    @DisplayName("Should throw exception when customer not found")
    void createVehicle_WhenCustomerNotFound_ShouldThrowException() {
        // Given
        VehicleDto newVehicleDto = new VehicleDto(
                null,
                "Honda",
                "Civic",
                2021,
                "XYZ789",
                "2HGBH41JXMN109187",
                "Red",
                30000,
                LocalDate.of(2024, 1, 1),
                Instant.now()
        );

        when(vehicleRepository.existsByLicensePlate("XYZ789")).thenReturn(false);
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(userDto));
        when(customerService.findByCustomerId(1)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> vehicleService.createVehicle(newVehicleDto, "testuser"))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessage("Customer not found");
        verify(vehicleRepository).existsByLicensePlate("XYZ789");
        verify(userService).findByUsername("testuser");
        verify(customerService).findByCustomerId(1);
        verifyNoInteractions(vehicleMapper, customerMapper);
    }

    @Test
    @DisplayName("Should throw exception when make is null or empty")
    void createVehicle_WhenMakeIsInvalid_ShouldThrowException() {
        // Given
        VehicleDto newVehicleDto = new VehicleDto(
                null,
                "", // Invalid make
                "Civic",
                2021,
                "XYZ789",
                "2HGBH41JXMN109187",
                "Red",
                30000,
                LocalDate.of(2024, 1, 1),
                Instant.now()
        );

        when(vehicleRepository.existsByLicensePlate("XYZ789")).thenReturn(false);
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(userDto));
        when(customerService.findByCustomerId(1)).thenReturn(Optional.of(customerDto));

        // When & Then
        assertThatThrownBy(() -> vehicleService.createVehicle(newVehicleDto, "testuser"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Vehicle make is required");
        verify(vehicleRepository).existsByLicensePlate("XYZ789");
        verify(userService).findByUsername("testuser");
        verify(customerService).findByCustomerId(1);
        verifyNoInteractions(vehicleMapper, customerMapper);
    }

    @Test
    @DisplayName("Should throw exception when model is null or empty")
    void createVehicle_WhenModelIsInvalid_ShouldThrowException() {
        // Given
        VehicleDto newVehicleDto = new VehicleDto(
                null,
                "Honda",
                "", // Invalid model
                2021,
                "XYZ789",
                "2HGBH41JXMN109187",
                "Red",
                30000,
                LocalDate.of(2024, 1, 1),
                Instant.now()
        );

        when(vehicleRepository.existsByLicensePlate("XYZ789")).thenReturn(false);
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(userDto));
        when(customerService.findByCustomerId(1)).thenReturn(Optional.of(customerDto));

        // When & Then
        assertThatThrownBy(() -> vehicleService.createVehicle(newVehicleDto, "testuser"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Vehicle model is required");
        verify(vehicleRepository).existsByLicensePlate("XYZ789");
        verify(userService).findByUsername("testuser");
        verify(customerService).findByCustomerId(1);
        verifyNoInteractions(vehicleMapper, customerMapper);
    }

    @Test
    @DisplayName("Should throw exception when year is invalid")
    void createVehicle_WhenYearIsInvalid_ShouldThrowException() {
        // Given
        VehicleDto newVehicleDto = new VehicleDto(
                null,
                "Honda",
                "Civic",
                1800, // Invalid year
                "XYZ789",
                "2HGBH41JXMN109187",
                "Red",
                30000,
                LocalDate.of(2024, 1, 1),
                Instant.now()
        );

        when(vehicleRepository.existsByLicensePlate("XYZ789")).thenReturn(false);
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(userDto));
        when(customerService.findByCustomerId(1)).thenReturn(Optional.of(customerDto));

        // When & Then
        assertThatThrownBy(() -> vehicleService.createVehicle(newVehicleDto, "testuser"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid vehicle year");
        verify(vehicleRepository).existsByLicensePlate("XYZ789");
        verify(userService).findByUsername("testuser");
        verify(customerService).findByCustomerId(1);
        verifyNoInteractions(vehicleMapper, customerMapper);
    }

    @Test
    @DisplayName("Should throw exception when license plate is null or empty")
    void createVehicle_WhenLicensePlateIsInvalid_ShouldThrowException() {
        // Given
        VehicleDto newVehicleDto = new VehicleDto(
                null,
                "Honda",
                "Civic",
                2021,
                "", // Invalid license plate
                "2HGBH41JXMN109187",
                "Red",
                30000,
                LocalDate.of(2024, 1, 1),
                Instant.now()
        );

        when(vehicleRepository.existsByLicensePlate("")).thenReturn(false);
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(userDto));
        when(customerService.findByCustomerId(1)).thenReturn(Optional.of(customerDto));

        // When & Then
        assertThatThrownBy(() -> vehicleService.createVehicle(newVehicleDto, "testuser"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("License plate is required");
        verify(vehicleRepository).existsByLicensePlate("");
        verify(userService).findByUsername("testuser");
        verify(customerService).findByCustomerId(1);
        verifyNoInteractions(vehicleMapper, customerMapper);
    }

    @Test
    @DisplayName("Should update vehicle successfully")
    void updateVehicle_WhenVehicleExists_ShouldUpdateAndReturnVehicle() {
        // Given
        VehicleDto updateDto = new VehicleDto(
                1,
                "Toyota",
                "Camry",
                2021, // Updated year
                "XYZ789", // Different license plate to test validation
                "1HGBH41JXMN109186",
                "Red", // Updated color
                60000, // Updated mileage
                LocalDate.of(2024, 1, 1), // Updated service date
                Instant.now()
        );

        Vehicle updatedVehicle = new Vehicle();
        updatedVehicle.setId(1);
        updatedVehicle.setMake("Toyota");
        updatedVehicle.setModel("Camry");
        updatedVehicle.setYear(2021);
        updatedVehicle.setLicensePlate("XYZ789");
        updatedVehicle.setColor("Red");
        updatedVehicle.setMileage(60000);

        VehicleDto expectedDto = new VehicleDto(
                1,
                "Toyota",
                "Camry",
                2021,
                "XYZ789",
                "1HGBH41JXMN109186",
                "Red",
                60000,
                LocalDate.of(2024, 1, 1),
                Instant.now()
        );

        when(vehicleRepository.findById(1)).thenReturn(Optional.of(vehicle));
        when(vehicleRepository.existsByLicensePlate("XYZ789")).thenReturn(false);
        when(vehicleMapper.partialUpdate(updateDto, vehicle)).thenReturn(updatedVehicle);
        when(vehicleRepository.save(updatedVehicle)).thenReturn(updatedVehicle);
        when(vehicleMapper.toDto(updatedVehicle)).thenReturn(expectedDto);

        // When
        Optional<VehicleDto> result = vehicleService.updateVehicle(1, updateDto);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(expectedDto);
        verify(vehicleRepository).findById(1);
        verify(vehicleRepository).existsByLicensePlate("XYZ789");
        verify(vehicleMapper).partialUpdate(updateDto, vehicle);
        verify(vehicleRepository).save(updatedVehicle);
        verify(vehicleMapper).toDto(updatedVehicle);
    }

    @Test
    @DisplayName("Should return empty when vehicle not found for update")
    void updateVehicle_WhenVehicleNotFound_ShouldReturnEmpty() {
        // Given
        VehicleDto updateDto = new VehicleDto(
                999,
                "Toyota",
                "Camry",
                2021,
                "ABC123",
                "1HGBH41JXMN109186",
                "Red",
                60000,
                LocalDate.of(2024, 1, 1),
                Instant.now()
        );

        when(vehicleRepository.findById(999)).thenReturn(Optional.empty());

        // When
        Optional<VehicleDto> result = vehicleService.updateVehicle(999, updateDto);

        // Then
        assertThat(result).isEmpty();
        verify(vehicleRepository).findById(999);
        verifyNoInteractions(vehicleMapper);
    }

    @Test
    @DisplayName("Should throw exception when license plate already exists during update")
    void updateVehicle_WhenLicensePlateExists_ShouldThrowException() {
        // Given
        VehicleDto updateDto = new VehicleDto(
                1,
                "Toyota",
                "Camry",
                2021,
                "XYZ789", // Different license plate that already exists
                "1HGBH41JXMN109186",
                "Red",
                60000,
                LocalDate.of(2024, 1, 1),
                Instant.now()
        );

        when(vehicleRepository.findById(1)).thenReturn(Optional.of(vehicle));
        when(vehicleRepository.existsByLicensePlate("XYZ789")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> vehicleService.updateVehicle(1, updateDto))
                .isInstanceOf(VehicleNotFoundException.class)
                .hasMessage("Vehicle with license plate XYZ789 already exists");
        verify(vehicleRepository).findById(1);
        verify(vehicleRepository).existsByLicensePlate("XYZ789");
        verifyNoInteractions(vehicleMapper);
    }

    @Test
    @DisplayName("Should delete vehicle successfully")
    void deleteVehicle_WhenVehicleExists_ShouldDeleteVehicle() {
        // Given
        when(vehicleRepository.existsById(1)).thenReturn(true);
        doNothing().when(vehicleRepository).deleteById(1);

        // When
        vehicleService.deleteVehicle(1);

        // Then
        verify(vehicleRepository).existsById(1);
        verify(vehicleRepository).deleteById(1);
    }

    @Test
    @DisplayName("Should throw exception when vehicle not found for deletion")
    void deleteVehicle_WhenVehicleNotFound_ShouldThrowException() {
        // Given
        when(vehicleRepository.existsById(999)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> vehicleService.deleteVehicle(999))
                .isInstanceOf(VehicleNotFoundException.class)
                .hasMessage("Vehicle with ID 999 not found");
        verify(vehicleRepository).existsById(999);
        verify(vehicleRepository, never()).deleteById(any());
    }
} 