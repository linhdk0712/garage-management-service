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
@Table(name = "parts_used")
public class PartsUsed {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parts_used_id_gen")
  @SequenceGenerator(
      name = "parts_used_id_gen",
      sequenceName = "parts_used_parts_used_id_seq",
      allocationSize = 1)
  @Column(name = "parts_used_id", nullable = false)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "work_order_id")
  private vn.utc.service.entity.WorkOrder workOrder;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "part_id")
  private vn.utc.service.entity.SparePart part;

  @NotNull
  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  @NotNull
  @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
  private BigDecimal unitPrice;

  @NotNull
  @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
  private BigDecimal totalPrice;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "used_at")
  private Instant usedAt;

    public PartsUsed setId(Integer id) {
        this.id = id;
        return this;
    }

    public PartsUsed setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
        return this;
    }

    public PartsUsed setPart(SparePart part) {
        this.part = part;
        return this;
    }

    public PartsUsed setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public PartsUsed setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public PartsUsed setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public PartsUsed setUsedAt(Instant usedAt) {
        this.usedAt = usedAt;
        return this;
    }
}
