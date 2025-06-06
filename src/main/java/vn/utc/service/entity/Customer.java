package vn.utc.service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;


@Getter
@Entity
@Table(name = "customers")
public class Customer {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customers_id_gen")
  @SequenceGenerator(
      name = "customers_id_gen",
      sequenceName = "customers_customer_id_seq",
      allocationSize = 1)
  @Column(name = "customer_id", nullable = false)
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

  @Size(max = 255)
  @Column(name = "address")
  private String address;

  @Size(max = 50)
  @Column(name = "city", length = 50)
  private String city;

  @Size(max = 50)
  @Column(name = "state", length = 50)
  private String state;

  @Size(max = 20)
  @Column(name = "zip_code", length = 20)
  private String zipCode;

  @Size(max = 20)
  @Column(name = "preferred_contact_method", length = 20)
  private String preferredContactMethod;

  @Column(name = "notes", length = Integer.MAX_VALUE)
  private String notes;

  @OneToMany(mappedBy = "customer")
  private Set<Appointment> appointments = new LinkedHashSet<>();

  @OneToMany(mappedBy = "customer")
  private Set<vn.utc.service.entity.Feedback> feedbacks = new LinkedHashSet<>();

  @OneToMany(mappedBy = "customer")
  private Set<vn.utc.service.entity.Vehicle> vehicles = new LinkedHashSet<>();

    public Customer setId(Integer id) {
        this.id = id;
        return this;
    }

    public Customer setUser(User user) {
        this.user = user;
        return this;
    }

    public Customer setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Customer setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Customer setAddress(String address) {
        this.address = address;
        return this;
    }

    public Customer setCity(String city) {
        this.city = city;
        return this;
    }

    public Customer setState(String state) {
        this.state = state;
        return this;
    }

    public Customer setZipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public Customer setPreferredContactMethod(String preferredContactMethod) {
        this.preferredContactMethod = preferredContactMethod;
        return this;
    }

    public Customer setNotes(String notes) {
        this.notes = notes;
        return this;
    }

    public Customer setAppointments(Set<Appointment> appointments) {
        this.appointments = appointments;
        return this;
    }

    public Customer setFeedbacks(Set<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
        return this;
    }

    public Customer setVehicles(Set<Vehicle> vehicles) {
        this.vehicles = vehicles;
        return this;
    }
}
