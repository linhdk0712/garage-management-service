package vn.utc.service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

/** Mapping for DB view */
@Getter
@Entity
@Immutable
@Table(name = "vehicle_service_history")
public class VehicleServiceHistory {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vehicle_service_history_id_gen")
  @SequenceGenerator(
      name = "vehicle_service_history_id_gen",
      sequenceName = "labor_charges_labor_charge_id_seq",
      allocationSize = 1)
  @Column(name = "vehicle_id")
  private Integer vehicleId;

  @Size(max = 50)
  @Column(name = "make", length = 50)
  private String make;

  @Size(max = 50)
  @Column(name = "model", length = 50)
  private String model;

  @Column(name = "year")
  private Integer year;

  @Size(max = 20)
  @Column(name = "license_plate", length = 20)
  private String licensePlate;

  @Column(name = "appointment_id")
  private Integer appointmentId;

  @Column(name = "appointment_date")
  private Instant appointmentDate;

  @Size(max = 100)
  @Column(name = "service_type", length = 100)
  private String serviceType;

  @Column(name = "work_order_id")
  private Integer workOrderId;

  @Column(name = "start_time")
  private Instant startTime;

  @Column(name = "end_time")
  private Instant endTime;

  @Size(max = 20)
  @Column(name = "work_order_status", length = 20)
  private String workOrderStatus;

  @Column(name = "total_cost", precision = 10, scale = 2)
  private BigDecimal totalCost;

  @Size(max = 100)
  @Column(name = "part_name", length = 100)
  private String partName;

  @Column(name = "part_quantity")
  private Integer partQuantity;

  @Column(name = "part_total_price", precision = 10, scale = 2)
  private BigDecimal partTotalPrice;

  @Column(name = "hours_worked", precision = 5, scale = 2)
  private BigDecimal hoursWorked;

  @Column(name = "labor_charge", precision = 10, scale = 2)
  private BigDecimal laborCharge;

  @Column(name = "rating")
  private Integer rating;

  @Column(name = "comments", length = Integer.MAX_VALUE)
  private String comments;

    public VehicleServiceHistory setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
        return this;
    }

    public VehicleServiceHistory setMake(String make) {
        this.make = make;
        return this;
    }

    public VehicleServiceHistory setModel(String model) {
        this.model = model;
        return this;
    }

    public VehicleServiceHistory setYear(Integer year) {
        this.year = year;
        return this;
    }

    public VehicleServiceHistory setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
        return this;
    }

    public VehicleServiceHistory setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
        return this;
    }

    public VehicleServiceHistory setAppointmentDate(Instant appointmentDate) {
        this.appointmentDate = appointmentDate;
        return this;
    }

    public VehicleServiceHistory setServiceType(String serviceType) {
        this.serviceType = serviceType;
        return this;
    }

    public VehicleServiceHistory setWorkOrderId(Integer workOrderId) {
        this.workOrderId = workOrderId;
        return this;
    }

    public VehicleServiceHistory setStartTime(Instant startTime) {
        this.startTime = startTime;
        return this;
    }

    public VehicleServiceHistory setEndTime(Instant endTime) {
        this.endTime = endTime;
        return this;
    }

    public VehicleServiceHistory setWorkOrderStatus(String workOrderStatus) {
        this.workOrderStatus = workOrderStatus;
        return this;
    }

    public VehicleServiceHistory setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
        return this;
    }

    public VehicleServiceHistory setPartName(String partName) {
        this.partName = partName;
        return this;
    }

    public VehicleServiceHistory setPartQuantity(Integer partQuantity) {
        this.partQuantity = partQuantity;
        return this;
    }

    public VehicleServiceHistory setPartTotalPrice(BigDecimal partTotalPrice) {
        this.partTotalPrice = partTotalPrice;
        return this;
    }

    public VehicleServiceHistory setHoursWorked(BigDecimal hoursWorked) {
        this.hoursWorked = hoursWorked;
        return this;
    }

    public VehicleServiceHistory setLaborCharge(BigDecimal laborCharge) {
        this.laborCharge = laborCharge;
        return this;
    }

    public VehicleServiceHistory setRating(Integer rating) {
        this.rating = rating;
        return this;
    }

    public VehicleServiceHistory setComments(String comments) {
        this.comments = comments;
        return this;
    }
}
