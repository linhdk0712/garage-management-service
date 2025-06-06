package vn.utc.service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter

@Entity
@Table(name = "purchase_orders")
public class PurchaseOrder {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchase_orders_id_gen")
  @SequenceGenerator(
      name = "purchase_orders_id_gen",
      sequenceName = "purchase_orders_purchase_order_id_seq",
      allocationSize = 1)
  @Column(name = "purchase_order_id", nullable = false)
  private Integer id;

  @Size(max = 100)
  @NotNull
  @Column(name = "supplier_name", nullable = false, length = 100)
  private String supplierName;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "order_date")
  private Instant orderDate;

  @Column(name = "expected_delivery_date")
  private LocalDate expectedDeliveryDate;

  @Size(max = 20)
  @Column(name = "status", length = 20)
  private String status;

  @Column(name = "total_amount", precision = 10, scale = 2)
  private BigDecimal totalAmount;

  @Column(name = "notes", length = Integer.MAX_VALUE)
  private String notes;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "created_by")
  private vn.utc.service.entity.Staff createdBy;

  @OneToMany(mappedBy = "purchaseOrder")
  private Set<PurchaseOrderItem> purchaseOrderItems = new LinkedHashSet<>();

    public PurchaseOrder setId(Integer id) {
        this.id = id;
        return this;
    }

    public PurchaseOrder setSupplierName(String supplierName) {
        this.supplierName = supplierName;
        return this;
    }

    public PurchaseOrder setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public PurchaseOrder setExpectedDeliveryDate(LocalDate expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
        return this;
    }

    public PurchaseOrder setStatus(String status) {
        this.status = status;
        return this;
    }

    public PurchaseOrder setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public PurchaseOrder setNotes(String notes) {
        this.notes = notes;
        return this;
    }

    public PurchaseOrder setCreatedBy(Staff createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public PurchaseOrder setPurchaseOrderItems(Set<PurchaseOrderItem> purchaseOrderItems) {
        this.purchaseOrderItems = purchaseOrderItems;
        return this;
    }
}
