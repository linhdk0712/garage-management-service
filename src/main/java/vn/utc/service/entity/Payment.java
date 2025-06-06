package vn.utc.service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter

@Entity
@Table(name = "payments")
public class Payment {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payments_id_gen")
  @SequenceGenerator(
      name = "payments_id_gen",
      sequenceName = "payments_payment_id_seq",
      allocationSize = 1)
  @Column(name = "payment_id", nullable = false)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "work_order_id")
  private vn.utc.service.entity.WorkOrder workOrder;

  @NotNull
  @Column(name = "amount", nullable = false, precision = 10, scale = 2)
  private BigDecimal amount;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "payment_date")
  private Instant paymentDate;

  @Size(max = 50)
  @Column(name = "payment_method", length = 50)
  private String paymentMethod;

  @Size(max = 20)
  @Column(name = "status", length = 20)
  private String status;

  @Size(max = 100)
  @Column(name = "transaction_id", length = 100)
  private String transactionId;

  @Column(name = "notes", length = Integer.MAX_VALUE)
  private String notes;

    public Payment setId(Integer id) {
        this.id = id;
        return this;
    }

    public Payment setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
        return this;
    }

    public Payment setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public Payment setPaymentDate(Instant paymentDate) {
        this.paymentDate = paymentDate;
        return this;
    }

    public Payment setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public Payment setStatus(String status) {
        this.status = status;
        return this;
    }

    public Payment setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public Payment setNotes(String notes) {
        this.notes = notes;
        return this;
    }
}
