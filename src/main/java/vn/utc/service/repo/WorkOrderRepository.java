package vn.utc.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.utc.service.entity.WorkOrder;

public interface WorkOrderRepository extends JpaRepository<WorkOrder, Integer> {}
