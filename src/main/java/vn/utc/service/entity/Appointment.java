package vn.utc.service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;


@Getter
@Entity
@Table(name = "appointments")

public class Appointment {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appointments_id_gen")
  @SequenceGenerator(
      name = "appointments_id_gen",
      sequenceName = "appointments_appointment_id_seq",
      allocationSize = 1)
  @Column(name = "appointment_id", nullable = false)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "vehicle_id")
  private vn.utc.service.entity.Vehicle vehicle;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id")
  private vn.utc.service.entity.Customer customer;

  @NotNull
  @Column(name = "appointment_date", nullable = false)
  private Instant appointmentDate;

  @Column(name = "estimated_completion")
  private Instant estimatedCompletion;

  @Size(max = 20)
  @Column(name = "status", length = 20)
  private String status;

  @Size(max = 100)
  @NotNull
  @Column(name = "service_type", nullable = false, length = 100)
  private String serviceType;

  @Column(name = "description", length = Integer.MAX_VALUE)
  private String description;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "created_at")
  private Instant createdAt;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "updated_at")
  private Instant updatedAt;

  @OneToMany(mappedBy = "appointment")
  private Set<vn.utc.service.entity.WorkOrder> workOrders = new LinkedHashSet<>();

    public Appointment setId(Integer id) {
    this.id = id;
    return this;
  }

    public Appointment setVehicle(Vehicle vehicle) {
    this.vehicle = vehicle;
    return this;
  }

    public Appointment setCustomer(Customer customer) {
    this.customer = customer;
    return this;
  }

    public Appointment setAppointmentDate(Instant appointmentDate) {
    this.appointmentDate = appointmentDate;
    return this;
  }

    public Appointment setEstimatedCompletion(Instant estimatedCompletion) {
    this.estimatedCompletion = estimatedCompletion;
    return this;
  }

    public Appointment setStatus(String status) {
    this.status = status;
    return this;
  }

    public Appointment setServiceType(String serviceType) {
    this.serviceType = serviceType;
    return this;
  }

    public Appointment setDescription(String description) {
    this.description = description;
    return this;
  }

    public Appointment setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
    return this;
  }

    public Appointment setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

    public Appointment setWorkOrders(Set<WorkOrder> workOrders) {
    this.workOrders = workOrders;
    return this;
  }
}
