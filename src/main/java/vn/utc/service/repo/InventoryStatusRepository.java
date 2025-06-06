package vn.utc.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.utc.service.entity.InventoryStatus;

public interface InventoryStatusRepository extends JpaRepository<InventoryStatus, Integer> {}
