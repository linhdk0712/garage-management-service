package vn.utc.service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter

@Entity
@Table(name = "vehicles")
public class Vehicle {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vehicles_id_gen")
  @SequenceGenerator(
      name = "vehicles_id_gen",
      sequenceName = "vehicles_vehicle_id_seq",
      allocationSize = 1)
  @Column(name = "vehicle_id", nullable = false)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id")
  private Customer customer;

  @Size(max = 50)
  @NotNull
  @Column(name = "make", nullable = false, length = 50)
  private String make;

  @Size(max = 50)
  @NotNull
  @Column(name = "model", nullable = false, length = 50)
  private String model;

  @NotNull
  @Column(name = "year", nullable = false)
  private Integer year;

  @Size(max = 20)
  @NotNull
  @Column(name = "license_plate", nullable = false, length = 20)
  private String licensePlate;

  @Size(max = 17)
  @Column(name = "vin", length = 17)
  private String vin;

  @Size(max = 30)
  @Column(name = "color", length = 30)
  private String color;

  @Column(name = "mileage")
  private Integer mileage;

  @Column(name = "last_service_date")
  private LocalDate lastServiceDate;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "registration_date")
  private Instant registrationDate;

  @OneToMany(mappedBy = "vehicle")
  private Set<Appointment> appointments = new LinkedHashSet<>();

    public Vehicle setId(Integer id) {
        this.id = id;
        return this;
    }

    public Vehicle setCustomer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public Vehicle setMake(String make) {
        this.make = make;
        return this;
    }

    public Vehicle setModel(String model) {
        this.model = model;
        return this;
    }

    public Vehicle setYear(Integer year) {
        this.year = year;
        return this;
    }

    public Vehicle setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
        return this;
    }

    public Vehicle setVin(String vin) {
        this.vin = vin;
        return this;
    }

    public Vehicle setColor(String color) {
        this.color = color;
        return this;
    }

    public Vehicle setMileage(Integer mileage) {
        this.mileage = mileage;
        return this;
    }

    public Vehicle setLastServiceDate(LocalDate lastServiceDate) {
        this.lastServiceDate = lastServiceDate;
        return this;
    }

    public Vehicle setRegistrationDate(Instant registrationDate) {
        this.registrationDate = registrationDate;
        return this;
    }

    public Vehicle setAppointments(Set<Appointment> appointments) {
        this.appointments = appointments;
        return this;
    }
}
