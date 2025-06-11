package vn.utc.service.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.utc.service.entity.WorkOrder;

import java.time.Instant;
import java.util.List;

public interface WorkOrderRepository extends JpaRepository<WorkOrder, Integer> {
    
    /**
     * Find all work orders assigned to a specific staff member
     */
    List<WorkOrder> findByStaffId(Integer staffId);
    
    /**
     * Find all work orders assigned to a specific staff member with pagination
     */
    Page<WorkOrder> findByStaffId(Integer staffId, Pageable pageable);
    
    /**
     * Find work orders by staff ID and status
     */
    List<WorkOrder> findByStaffIdAndStatus(Integer staffId, String status);
    
    /**
     * Find work orders by staff ID and status with pagination
     */
    Page<WorkOrder> findByStaffIdAndStatus(Integer staffId, String status, Pageable pageable);
    
    /**
     * Find work orders by staff ID and date range
     */
    @Query("SELECT wo FROM WorkOrder wo WHERE wo.staff.id = :staffId AND wo.startTime >= :fromDate AND wo.startTime <= :toDate")
    List<WorkOrder> findByStaffIdAndDateRange(@Param("staffId") Integer staffId, 
                                             @Param("fromDate") Instant fromDate, 
                                             @Param("toDate") Instant toDate);
    
    /**
     * Find work orders by staff ID and date range with pagination
     */
    @Query("SELECT wo FROM WorkOrder wo WHERE wo.staff.id = :staffId AND wo.startTime >= :fromDate AND wo.startTime <= :toDate")
    Page<WorkOrder> findByStaffIdAndDateRange(@Param("staffId") Integer staffId, 
                                             @Param("fromDate") Instant fromDate, 
                                             @Param("toDate") Instant toDate, 
                                             Pageable pageable);
    
    /**
     * Find work orders by staff ID, status, and date range
     */
    @Query("SELECT wo FROM WorkOrder wo WHERE wo.staff.id = :staffId AND wo.status = :status AND wo.startTime >= :fromDate AND wo.startTime <= :toDate")
    List<WorkOrder> findByStaffIdAndStatusAndDateRange(@Param("staffId") Integer staffId, 
                                                      @Param("status") String status,
                                                      @Param("fromDate") Instant fromDate, 
                                                      @Param("toDate") Instant toDate);
    
    /**
     * Find work orders by staff ID, status, and date range with pagination
     */
    @Query("SELECT wo FROM WorkOrder wo WHERE wo.staff.id = :staffId AND wo.status = :status AND wo.startTime >= :fromDate AND wo.startTime <= :toDate")
    Page<WorkOrder> findByStaffIdAndStatusAndDateRange(@Param("staffId") Integer staffId, 
                                                      @Param("status") String status,
                                                      @Param("fromDate") Instant fromDate, 
                                                      @Param("toDate") Instant toDate, 
                                                      Pageable pageable);
}
