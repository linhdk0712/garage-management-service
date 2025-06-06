package vn.utc.service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Entity
@Table(name = "work_orders")
public class WorkOrder {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "work_orders_id_gen")
  @SequenceGenerator(
      name = "work_orders_id_gen",
      sequenceName = "work_orders_work_order_id_seq",
      allocationSize = 1)
  @Column(name = "work_order_id", nullable = false)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "appointment_id")
  private Appointment appointment;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "staff_id")
  private Staff staff;

  @Column(name = "start_time")
  private Instant startTime;

  @Column(name = "end_time")
  private Instant endTime;

  @Size(max = 20)
  @Column(name = "status", length = 20)
  private String status;

  @Column(name = "diagnostic_notes", length = Integer.MAX_VALUE)
  private String diagnosticNotes;

  @Column(name = "total_cost", precision = 10, scale = 2)
  private BigDecimal totalCost;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "created_at")
  private Instant createdAt;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "updated_at")
  private Instant updatedAt;

  @OneToMany(mappedBy = "workOrder")
  private Set<Feedback> feedbacks = new LinkedHashSet<>();

  @OneToMany(mappedBy = "workOrder")
  private Set<LaborCharge> laborCharges = new LinkedHashSet<>();

    public WorkOrder setId(Integer id) {
        this.id = id;
        return this;
    }

    public WorkOrder setAppointment(Appointment appointment) {
        this.appointment = appointment;
        return this;
    }

    public WorkOrder setStaff(Staff staff) {
        this.staff = staff;
        return this;
    }

    public WorkOrder setStartTime(Instant startTime) {
        this.startTime = startTime;
        return this;
    }

    public WorkOrder setEndTime(Instant endTime) {
        this.endTime = endTime;
        return this;
    }

    public WorkOrder setStatus(String status) {
        this.status = status;
        return this;
    }

    public WorkOrder setDiagnosticNotes(String diagnosticNotes) {
        this.diagnosticNotes = diagnosticNotes;
        return this;
    }

    public WorkOrder setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
        return this;
    }

    public WorkOrder setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public WorkOrder setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public WorkOrder setFeedbacks(Set<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
        return this;
    }

    public WorkOrder setLaborCharges(Set<LaborCharge> laborCharges) {
        this.laborCharges = laborCharges;
        return this;
    }
}
