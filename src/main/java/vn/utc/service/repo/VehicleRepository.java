package vn.utc.service.repo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.utc.service.entity.Vehicle;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
    boolean existsByLicensePlate(@Size(max = 20) @NotNull String licensePlate);

    List<Vehicle> findVehiclesByCustomerId(int id);
    
    Page<Vehicle> findVehiclesByCustomerId(int id, Pageable pageable);
    
    @Query("SELECT v FROM Vehicle v WHERE " +
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(v.make) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(v.model) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(v.licensePlate) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(v.vin) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:make IS NULL OR :make = '' OR LOWER(v.make) = LOWER(:make)) AND " +
           "(:model IS NULL OR :model = '' OR LOWER(v.model) = LOWER(:model)) AND " +
           "(:year IS NULL OR v.year = :year) AND " +
           "(:customerId IS NULL OR v.customer.id = :customerId)")
    Page<Vehicle> findByFilters(@Param("search") String search,
                               @Param("make") String make,
                               @Param("model") String model,
                               @Param("year") Integer year,
                               @Param("customerId") Integer customerId,
                               Pageable pageable);
}
