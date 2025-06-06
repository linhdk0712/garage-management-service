package vn.utc.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.utc.service.entity.PurchaseOrderItem;

public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItem, Integer> {}
