package vn.utc.service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter

@Entity
@Table(name = "labor_charges")
public class LaborCharge {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "labor_charges_id_gen")
  @SequenceGenerator(
      name = "labor_charges_id_gen",
      sequenceName = "labor_charges_labor_charge_id_seq",
      allocationSize = 1)
  @Column(name = "labor_charge_id", nullable = false)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "work_order_id")
  private vn.utc.service.entity.WorkOrder workOrder;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "staff_id")
  private vn.utc.service.entity.Staff staff;

  @NotNull
  @Column(name = "hours_worked", nullable = false, precision = 5, scale = 2)
  private BigDecimal hoursWorked;

  @NotNull
  @Column(name = "rate_per_hour", nullable = false, precision = 10, scale = 2)
  private BigDecimal ratePerHour;

  @NotNull
  @Column(name = "total_charge", nullable = false, precision = 10, scale = 2)
  private BigDecimal totalCharge;

  @Column(name = "description", length = Integer.MAX_VALUE)
  private String description;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "recorded_at")
  private Instant recordedAt;

    public LaborCharge setId(Integer id) {
        this.id = id;
        return this;
    }

    public LaborCharge setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
        return this;
    }

    public LaborCharge setStaff(Staff staff) {
        this.staff = staff;
        return this;
    }

    public LaborCharge setHoursWorked(BigDecimal hoursWorked) {
        this.hoursWorked = hoursWorked;
        return this;
    }

    public LaborCharge setRatePerHour(BigDecimal ratePerHour) {
        this.ratePerHour = ratePerHour;
        return this;
    }

    public LaborCharge setTotalCharge(BigDecimal totalCharge) {
        this.totalCharge = totalCharge;
        return this;
    }

    public LaborCharge setDescription(String description) {
        this.description = description;
        return this;
    }

    public LaborCharge setRecordedAt(Instant recordedAt) {
        this.recordedAt = recordedAt;
        return this;
    }
}
