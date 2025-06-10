package vn.utc.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.utc.service.entity.InventoryStatus;

@Repository
public interface InventoryStatusRepository extends JpaRepository<InventoryStatus, Integer> {}
