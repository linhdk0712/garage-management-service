package vn.utc.service.repo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.utc.service.entity.Vehicle;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
    boolean existsByLicensePlate(@Size(max = 20) @NotNull String licensePlate);

    List<Vehicle> findVehiclesByCustomerId(int id);
}
