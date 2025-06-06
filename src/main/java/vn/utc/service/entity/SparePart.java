package vn.utc.service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "spare_parts")
public class SparePart {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "spare_parts_id_gen")
  @SequenceGenerator(
      name = "spare_parts_id_gen",
      sequenceName = "spare_parts_part_id_seq",
      allocationSize = 1)
  @Column(name = "part_id", nullable = false)
  private Integer id;

  @Size(max = 100)
  @NotNull
  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "description", length = Integer.MAX_VALUE)
  private String description;

  @Size(max = 50)
  @Column(name = "category", length = 50)
  private String category;

  @NotNull
  @Column(name = "price", nullable = false, precision = 10, scale = 2)
  private BigDecimal price;

  @NotNull
  @Column(name = "cost", nullable = false, precision = 10, scale = 2)
  private BigDecimal cost;

  @NotNull
  @Column(name = "quantity_in_stock", nullable = false)
  private Integer quantityInStock;

  @Column(name = "minimum_stock_level")
  private Integer minimumStockLevel;

  @Size(max = 50)
  @Column(name = "location", length = 50)
  private String location;

  @Size(max = 100)
  @Column(name = "supplier", length = 100)
  private String supplier;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "created_at")
  private Instant createdAt;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "updated_at")
  private Instant updatedAt;

  @OneToMany(mappedBy = "part")
  private Set<PartsUsed> partsUseds = new LinkedHashSet<>();

  @OneToMany(mappedBy = "part")
  private Set<PurchaseOrderItem> purchaseOrderItems = new LinkedHashSet<>();

    public SparePart setId(Integer id) {
        this.id = id;
        return this;
    }

    public SparePart setName(String name) {
        this.name = name;
        return this;
    }

    public SparePart setDescription(String description) {
        this.description = description;
        return this;
    }

    public SparePart setCategory(String category) {
        this.category = category;
        return this;
    }

    public SparePart setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public SparePart setCost(BigDecimal cost) {
        this.cost = cost;
        return this;
    }

    public SparePart setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
        return this;
    }

    public SparePart setMinimumStockLevel(Integer minimumStockLevel) {
        this.minimumStockLevel = minimumStockLevel;
        return this;
    }

    public SparePart setLocation(String location) {
        this.location = location;
        return this;
    }

    public SparePart setSupplier(String supplier) {
        this.supplier = supplier;
        return this;
    }

    public SparePart setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public SparePart setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public SparePart setPartsUseds(Set<PartsUsed> partsUseds) {
        this.partsUseds = partsUseds;
        return this;
    }

    public SparePart setPurchaseOrderItems(Set<PurchaseOrderItem> purchaseOrderItems) {
        this.purchaseOrderItems = purchaseOrderItems;
        return this;
    }
}
