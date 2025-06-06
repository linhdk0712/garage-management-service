package vn.utc.service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "purchase_order_items")
public class PurchaseOrderItem {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchase_order_items_id_gen")
  @SequenceGenerator(
      name = "purchase_order_items_id_gen",
      sequenceName = "purchase_order_items_item_id_seq",
      allocationSize = 1)
  @Column(name = "item_id", nullable = false)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "purchase_order_id")
  private vn.utc.service.entity.PurchaseOrder purchaseOrder;

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

    public PurchaseOrderItem setId(Integer id) {
        this.id = id;
        return this;
    }

    public PurchaseOrderItem setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
        return this;
    }

    public PurchaseOrderItem setPart(SparePart part) {
        this.part = part;
        return this;
    }

    public PurchaseOrderItem setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public PurchaseOrderItem setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public PurchaseOrderItem setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }
}
