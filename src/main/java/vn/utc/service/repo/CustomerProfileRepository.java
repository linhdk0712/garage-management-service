package vn.utc.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.utc.service.entity.CustomerProfile;

import java.util.Optional;

public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, Integer> {
    Optional<CustomerProfile> findCustomerProfileByUserId(Integer id);
    Optional<CustomerProfile> findCustomerProfileByUsername(String username);
}
