package vn.utc.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.utc.service.entity.Staff;
import vn.utc.service.entity.User;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Integer> {
  Optional<Staff> findStaffByUser(User user);
  }