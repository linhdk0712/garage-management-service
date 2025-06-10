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
}
