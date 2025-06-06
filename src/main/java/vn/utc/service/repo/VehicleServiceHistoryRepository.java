package vn.utc.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.utc.service.entity.VehicleServiceHistory;

public interface VehicleServiceHistoryRepository
    extends JpaRepository<VehicleServiceHistory, Integer> {}
