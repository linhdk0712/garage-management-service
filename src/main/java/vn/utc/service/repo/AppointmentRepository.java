package vn.utc.service.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.utc.service.entity.Appointment;

import java.time.Instant;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    
    @Query("SELECT a FROM Appointment a WHERE a.customer.id = :customerId")
    Page<Appointment> findByCustomerId(@Param("customerId") Integer customerId, Pageable pageable);
    
    @Query("SELECT a FROM Appointment a WHERE a.customer.id = :customerId AND " +
           "(:status IS NULL OR :status = '' OR LOWER(a.status) = LOWER(:status))")
    Page<Appointment> findByCustomerIdAndStatus(@Param("customerId") Integer customerId,
                                               @Param("status") String status,
                                               Pageable pageable);
    
    @Query("SELECT a FROM Appointment a WHERE a.customer.id = :customerId AND " +
           "date(a.appointmentDate) >= date(:from) AND date(a.appointmentDate) <= date(:to)")
    Page<Appointment> findByCustomerIdAndDateRange(@Param("customerId") Integer customerId,
                                                  @Param("from") Instant from,
                                                  @Param("to") Instant to,
                                                  Pageable pageable);
    
    @Query("SELECT a FROM Appointment a WHERE a.customer.id = :customerId AND " +
           "(:status IS NULL OR :status = '' OR LOWER(a.status) = LOWER(:status)) AND " +
           "date(a.appointmentDate) >= date(:from) AND date(a.appointmentDate) <= date(:to)")
    Page<Appointment> findByCustomerIdAndStatusAndDateRange(@Param("customerId") Integer customerId,
                                                           @Param("status") String status,
                                                           @Param("from") Instant from,
                                                           @Param("to") Instant to,
                                                           Pageable pageable);
    
    @Query("SELECT a FROM Appointment a WHERE " +
           "(:status IS NULL OR :status = '' OR LOWER(a.status) = LOWER(:status))")
    Page<Appointment> findByStatus(@Param("status") String status, Pageable pageable);
    
    @Query("SELECT a FROM Appointment a WHERE " +
           "date(a.appointmentDate) >= date(:from) AND date(a.appointmentDate) <= date(:to)")
    Page<Appointment> findByDateRange(@Param("from") Instant from,
                                     @Param("to") Instant to,
                                     Pageable pageable);
    
    @Query("SELECT a FROM Appointment a WHERE " +
           "(:status IS NULL OR :status = '' OR LOWER(a.status) = LOWER(:status)) AND " +
           "date(a.appointmentDate) >= date(:from) AND date(a.appointmentDate) <= date(:to)")
    Page<Appointment> findByStatusAndDateRange(@Param("status") String status,
                                              @Param("from") Instant from,
                                              @Param("to") Instant to,
                                              Pageable pageable);
}
