package vn.utc.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.utc.service.entity.StaffProfile;

import java.util.Optional;

public interface StaffProfileRepository extends JpaRepository<StaffProfile, Integer> {
  Optional<StaffProfile> findByUsername(String username);
}
