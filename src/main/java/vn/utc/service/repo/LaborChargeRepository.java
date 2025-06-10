package vn.utc.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.utc.service.entity.LaborCharge;

@Repository
public interface LaborChargeRepository extends JpaRepository<LaborCharge, Integer> {}
