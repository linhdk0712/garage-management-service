package vn.utc.service.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.utc.service.dtos.CustomerDto;
import vn.utc.service.dtos.UserDto;
import vn.utc.service.dtos.VehicleDto;
import vn.utc.service.dtos.VehicleHealthDto;
import vn.utc.service.dtos.ComponentHealthDto;
import vn.utc.service.dtos.ComponentStatusDto;
import vn.utc.service.dtos.HealthHistoryDto;
import vn.utc.service.dtos.MaintenanceItemDto;
import vn.utc.service.entity.Vehicle;
import vn.utc.service.exception.CustomerNotFoundException;
import vn.utc.service.exception.UserNotFoundException;
import vn.utc.service.exception.VehicleNotFoundException;
import vn.utc.service.mapper.CustomerMapper;
import vn.utc.service.mapper.VehicleMapper;
import vn.utc.service.repo.VehicleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleService {

  private final VehicleRepository vehicleRepository;
  private final VehicleMapper vehicleMapper;
  private final CustomerService customerService;
  private final UserService userService;
  private final CustomerMapper customerMapper;

  public List<VehicleDto> getAllVehicles() {
    List<Vehicle> vehicles = vehicleRepository.findAll();
    List<VehicleDto> vehicleDtos = new ArrayList<>();
    vehicles.forEach(
        v -> {
          VehicleDto vehicleDto = vehicleMapper.toDto(v);
          vehicleDtos.add(vehicleDto);
        });
    return vehicleDtos;
  }

  public Page<VehicleDto> getAllVehicles(Pageable pageable) {
    return vehicleRepository.findAll(pageable)
        .map(vehicleMapper::toDto);
  }

  public Page<VehicleDto> getAllVehicles(Pageable pageable, String search, String make, String model, Integer year, Integer customerId) {
    if (search != null && !search.trim().isEmpty() || 
        make != null && !make.trim().isEmpty() || 
        model != null && !model.trim().isEmpty() || 
        year != null || customerId != null) {
      return vehicleRepository.findByFilters(search, make, model, year, customerId, pageable)
          .map(vehicleMapper::toDto);
    }
    return vehicleRepository.findAll(pageable)
        .map(vehicleMapper::toDto);
  }

  public List<VehicleDto> getVehiclesByCustomerId(int id) {
    List<Vehicle> vehicles = vehicleRepository.findVehiclesByCustomerId(id);
    List<VehicleDto> vehicleDtos = new ArrayList<>();
    vehicles.forEach(
        v -> {
          VehicleDto vehicleDto = vehicleMapper.toDto(v);
          vehicleDtos.add(vehicleDto);
        });
    return vehicleDtos;
  }

  public Page<VehicleDto> getVehiclesByCustomerId(int id, Pageable pageable) {
    return vehicleRepository.findVehiclesByCustomerId(id, pageable)
        .map(vehicleMapper::toDto);
  }

  public Optional<VehicleDto> getVehicleById(Integer id) {
    Vehicle vehicle = vehicleRepository.findById(id).orElse(null);
    return vehicle == null ? Optional.empty() : Optional.of(vehicleMapper.toDto(vehicle));
  }

  @Transactional
  public VehicleDto createVehicle(VehicleDto vehicle, String username) {
    if (vehicleRepository.existsByLicensePlate(vehicle.licensePlate())) {
      throw new VehicleNotFoundException(
          "Vehicle with license plate " + vehicle.licensePlate() + " already exists");
    }
    UserDto userDto = userService.findByUsername(username).orElse(null);
    if (userDto == null) {
      throw new UserNotFoundException("User not found");
    }
    CustomerDto customerDto = customerService.findByCustomerId(userDto.getId()).orElse(null);
    if (customerDto == null) {
      throw new CustomerNotFoundException("Customer not found");
    }
    
    // Validate vehicle data
    if (vehicle.make() == null || vehicle.make().trim().isEmpty()) {
      throw new IllegalArgumentException("Vehicle make is required");
    }
    if (vehicle.model() == null || vehicle.model().trim().isEmpty()) {
      throw new IllegalArgumentException("Vehicle model is required");
    }
    if (vehicle.year() == null || vehicle.year() < 1900 || vehicle.year() > (java.time.Year.now().getValue() + 1)) {
      throw new IllegalArgumentException("Invalid vehicle year");
    }
    if (vehicle.licensePlate() == null || vehicle.licensePlate().trim().isEmpty()) {
      throw new IllegalArgumentException("License plate is required");
    }
    
    Vehicle vehicleEntity = vehicleMapper.toEntity(vehicle);
    vehicleEntity.setCustomer(customerMapper.toEntity(customerDto));
    return vehicleMapper.toDto(vehicleRepository.save(vehicleEntity));
  }

  public Optional<VehicleDto> updateVehicle(Integer id, VehicleDto vehicleDto) {
    Optional<Vehicle> vehicle = vehicleRepository.findById(id);
    if (vehicle.isPresent()) {
      Vehicle vehicleEntity = vehicle.get();
      
      // Check if license plate is being changed and if it already exists
      if (!vehicleEntity.getLicensePlate().equals(vehicleDto.licensePlate()) && 
          vehicleRepository.existsByLicensePlate(vehicleDto.licensePlate())) {
        throw new VehicleNotFoundException(
            "Vehicle with license plate " + vehicleDto.licensePlate() + " already exists");
      }
      
      vehicleEntity = vehicleMapper.partialUpdate(vehicleDto, vehicleEntity);
      return Optional.of(vehicleMapper.toDto(vehicleRepository.save(vehicleEntity)));
    }
    return Optional.empty();
  }

  public void deleteVehicle(Integer id) {
    if (!vehicleRepository.existsById(id)) {
      throw new VehicleNotFoundException("Vehicle with ID " + id + " not found");
    }
    vehicleRepository.deleteById(id);
  }

  /**
   * Save multiple vehicles for data initialization purposes
   * @param vehicles List of Vehicle entities to save
   * @return List of saved Vehicle entities
   */
  @Transactional
  public List<Vehicle> saveAllForInitialization(List<Vehicle> vehicles) {
    return vehicleRepository.saveAll(vehicles);
  }

  /**
   * Find vehicles by customer ID for data initialization purposes
   * @param customerId The customer ID to search for
   * @return List of Vehicle entities
   */
  public List<Vehicle> findVehiclesByCustomerIdForInitialization(Integer customerId) {
    return vehicleRepository.findVehiclesByCustomerId(customerId);
  }

  /**
   * Get vehicle health data by vehicleId (stub implementation)
   */
  public VehicleHealthDto getVehicleHealthById(Integer vehicleId) {
    // TODO: Replace with real logic to calculate health from service history, inspections, etc.
    return new VehicleHealthDto(
        new ComponentHealthDto(85, "Engine running smoothly."),
        new ComponentHealthDto(80, "Transmission fluid replaced recently."),
        new ComponentHealthDto(75, "Brake pads will need replacement soon."),
        new ComponentHealthDto(90, "Suspension in good condition."),
        new ComponentHealthDto(70, "Battery may need checkup."),
        new ComponentStatusDto(4, 1, 0),
        "up",
        5,
        java.util.List.of(
            new HealthHistoryDto("2024-05-01", 70),
            new HealthHistoryDto("2024-06-01", 75),
            new HealthHistoryDto("2024-07-01", 80),
            new HealthHistoryDto("2024-08-01", 85)
        )
    );
  }

  /**
   * Get maintenance schedule for a vehicle by vehicleId (stub implementation)
   */
  public java.util.List<MaintenanceItemDto> getMaintenanceScheduleByVehicleId(Integer vehicleId) {
    // TODO: Replace with real logic to fetch maintenance schedule from DB
    return java.util.List.of(
        new MaintenanceItemDto(1, "Oil Change", "2024-09-01", 15000, "UPCOMING", "MEDIUM", "Change engine oil and filter."),
        new MaintenanceItemDto(2, "Brake Inspection", "2024-08-15", 16000, "OVERDUE", "HIGH", "Inspect and replace brake pads if needed."),
        new MaintenanceItemDto(3, "Tire Rotation", "2024-10-01", 17000, "UPCOMING", "LOW", "Rotate tires for even wear.")
    );
  }
}
