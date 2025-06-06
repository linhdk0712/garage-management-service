package vn.utc.service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

/** Mapping for DB view */
@Getter

@Entity
@Immutable
@Table(name = "inventory_status")
public class InventoryStatus {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inventory_status_id_gen")
  @SequenceGenerator(
      name = "inventory_status_id_gen",
      sequenceName = "labor_charges_labor_charge_id_seq",
      allocationSize = 1)
  @Column(name = "part_id")
  private Integer partId;

  @Size(max = 100)
  @Column(name = "name", length = 100)
  private String name;

  @Column(name = "description", length = Integer.MAX_VALUE)
  private String description;

  @Size(max = 50)
  @Column(name = "category", length = 50)
  private String category;

  @Column(name = "price", precision = 10, scale = 2)
  private BigDecimal price;

  @Column(name = "cost", precision = 10, scale = 2)
  private BigDecimal cost;

  @Column(name = "quantity_in_stock")
  private Integer quantityInStock;

  @Column(name = "minimum_stock_level")
  private Integer minimumStockLevel;

  @Column(name = "stock_status", length = Integer.MAX_VALUE)
  private String stockStatus;

  @Size(max = 50)
  @Column(name = "location", length = 50)
  private String location;

  @Size(max = 100)
  @Column(name = "supplier", length = 100)
  private String supplier;

    public InventoryStatus setPartId(Integer partId) {
        this.partId = partId;
        return this;
    }

    public InventoryStatus setName(String name) {
        this.name = name;
        return this;
    }

    public InventoryStatus setDescription(String description) {
        this.description = description;
        return this;
    }

    public InventoryStatus setCategory(String category) {
        this.category = category;
        return this;
    }

    public InventoryStatus setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public InventoryStatus setCost(BigDecimal cost) {
        this.cost = cost;
        return this;
    }

    public InventoryStatus setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
        return this;
    }

    public InventoryStatus setMinimumStockLevel(Integer minimumStockLevel) {
        this.minimumStockLevel = minimumStockLevel;
        return this;
    }

    public InventoryStatus setStockStatus(String stockStatus) {
        this.stockStatus = stockStatus;
        return this;
    }

    public InventoryStatus setLocation(String location) {
        this.location = location;
        return this;
    }

    public InventoryStatus setSupplier(String supplier) {
        this.supplier = supplier;
        return this;
    }
}
