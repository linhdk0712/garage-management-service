package vn.utc.service.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.utc.service.entity.CustomerProfile;

import java.util.Optional;

@Repository
public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, Integer> {
    Optional<CustomerProfile> findCustomerProfileByUserId(Integer id);
    Optional<CustomerProfile> findCustomerProfileByUsername(String username);
    
    @Query("SELECT cp FROM CustomerProfile cp WHERE " +
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(cp.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(cp.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(cp.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(cp.phone) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:status IS NULL OR :status = '' OR " +
           "(:status = 'ACTIVE' AND cp.isActive = true) OR " +
           "(:status = 'INACTIVE' AND cp.isActive = false))")
    Page<CustomerProfile> findBySearchAndStatus(@Param("search") String search, 
                                               @Param("status") String status, 
                                               Pageable pageable);
}
