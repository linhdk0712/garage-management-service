package vn.utc.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.utc.service.entity.Staff;

public interface StaffRepository extends JpaRepository<Staff, Integer> {
  }