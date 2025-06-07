package vn.utc.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

/** Mapping for DB view */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "customer_profile")
public class CustomerProfile {
  @Id
  @Column(name = "user_id")
  private Integer userId;

  @Size(max = 50)
  @Column(name = "username", length = 50)
  private String username;

  @Size(max = 100)
  @Column(name = "email", length = 100)
  private String email;

  @Size(max = 20)
  @Column(name = "phone", length = 20)
  private String phone;

  @Column(name = "created_at")
  private Instant createdAt;

  @Column(name = "last_login")
  private Instant lastLogin;

  @Column(name = "is_active")
  private Boolean isActive;

  @Size(max = 50)
  @Column(name = "first_name", length = 50)
  private String firstName;

  @Size(max = 50)
  @Column(name = "last_name", length = 50)
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

  @Column(name = "notes", length = Integer.MAX_VALUE)
  private String notes;
}
