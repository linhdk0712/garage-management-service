package vn.utc.service.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.utc.service.entity.Appointment;

import java.time.Instant;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    
    @Query("SELECT a FROM Appointment a WHERE a.customer.id = :customerId AND " +
           "(:status IS NULL OR :status = '' OR LOWER(a.status) = LOWER(:status)) AND " +
           "(:from IS NULL OR :from = '' OR a.appointmentDate >= :from) AND " +
           "(:to IS NULL OR :to = '' OR a.appointmentDate <= :to) AND " +
           "(:date IS NULL OR :date = '' OR DATE(a.appointmentDate) = DATE(:date))")
    Page<Appointment> findByCustomerAndFilters(@Param("customerId") Integer customerId,
                                             @Param("status") String status,
                                             @Param("from") String from,
                                             @Param("to") String to,
                                             @Param("date") String date,
                                             Pageable pageable);
    
    @Query("SELECT a FROM Appointment a WHERE " +
           "(:status IS NULL OR :status = '' OR LOWER(a.status) = LOWER(:status)) AND " +
           "(:from IS NULL OR :from = '' OR a.appointmentDate >= :from) AND " +
           "(:to IS NULL OR :to = '' OR a.appointmentDate <= :to) AND " +
           "(:date IS NULL OR :date = '' OR DATE(a.appointmentDate) = DATE(:date))")
    Page<Appointment> findByFilters(@Param("status") String status,
                                   @Param("from") String from,
                                   @Param("to") String to,
                                   @Param("date") String date,
                                   Pageable pageable);
}
