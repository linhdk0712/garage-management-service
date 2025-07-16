package vn.utc.service.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.utc.service.entity.Customer;
import vn.utc.service.entity.User;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Optional<Customer> findCustomerByUser(User user);
    Optional<Customer> findCustomerByUserId(Integer userId);
    Optional<Customer> findCustomerById(Integer id);
    
    @Query("SELECT c FROM Customer c WHERE " +
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(c.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.user.email) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:status IS NULL OR :status = '' OR " +
           "(:status = 'ACTIVE' AND c.user.isActive = true) OR " +
           "(:status = 'INACTIVE' AND c.user.isActive = false))")
    Page<Customer> findBySearchAndStatus(@Param("search") String search,
                                       @Param("status") String status,
                                       Pageable pageable);
}
