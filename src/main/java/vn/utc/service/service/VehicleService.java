package vn.utc.service.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    Vehicle vehicleEntity = vehicleMapper.toEntity(vehicle);
    vehicleEntity.setCustomer(customerMapper.toEntity(customerDto));
    return vehicleMapper.toDto(vehicleRepository.save(vehicleEntity));
  }

  public Optional<VehicleDto> updateVehicle(Integer id, VehicleDto vehicleDto) {
    Optional<Vehicle> vehicle = vehicleRepository.findById(id);
    if (vehicle.isPresent()) {
      Vehicle vehicleEntity = vehicle.get();
      vehicleEntity = vehicleMapper.partialUpdate(vehicleDto, vehicleEntity);
      return Optional.of(vehicleMapper.toDto(vehicleRepository.save(vehicleEntity)));
    }
    return Optional.empty();
  }

  public void deleteVehicle(Integer id) {
    vehicleRepository.deleteById(id);
  }
}
