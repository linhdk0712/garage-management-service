package vn.utc.service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "staff")
public class Staff {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "staff_id_gen")
  @SequenceGenerator(name = "staff_id_gen", sequenceName = "staff_staff_id_seq", allocationSize = 1)
  @Column(name = "staff_id", nullable = false)
  private Integer id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private vn.utc.service.entity.User user;

  @Size(max = 50)
  @NotNull
  @Column(name = "first_name", nullable = false, length = 50)
  private String firstName;

  @Size(max = 50)
  @NotNull
  @Column(name = "last_name", nullable = false, length = 50)
  private String lastName;

  @Size(max = 50)
  @Column(name = "\"position\"", length = 50)
  private String position;

  @Size(max = 100)
  @Column(name = "specialization", length = 100)
  private String specialization;

  @Column(name = "hire_date")
  private LocalDate hireDate;

  @Column(name = "hourly_rate", precision = 10, scale = 2)
  private BigDecimal hourlyRate;

  @OneToMany(mappedBy = "staff")
  private Set<LaborCharge> laborCharges = new LinkedHashSet<>();

  @OneToMany(mappedBy = "createdBy")
  private Set<PurchaseOrder> purchaseOrders = new LinkedHashSet<>();

  @OneToMany(mappedBy = "staff")
  private Set<vn.utc.service.entity.WorkOrder> workOrders = new LinkedHashSet<>();

    public Staff setId(Integer id) {
        this.id = id;
        return this;
    }

    public Staff setUser(User user) {
        this.user = user;
        return this;
    }

    public Staff setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Staff setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Staff setPosition(String position) {
        this.position = position;
        return this;
    }

    public Staff setSpecialization(String specialization) {
        this.specialization = specialization;
        return this;
    }

    public Staff setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
        return this;
    }

    public Staff setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
        return this;
    }

    public Staff setLaborCharges(Set<LaborCharge> laborCharges) {
        this.laborCharges = laborCharges;
        return this;
    }

    public Staff setPurchaseOrders(Set<PurchaseOrder> purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
        return this;
    }

    public Staff setWorkOrders(Set<WorkOrder> workOrders) {
        this.workOrders = workOrders;
        return this;
    }
}
