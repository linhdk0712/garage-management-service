package vn.utc.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

/** Mapping for DB view */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "staff_profile")
public class StaffProfile {
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

  @Column(name = "hire_date")
  private LocalDate hireDate;

  @Column(name = "hourly_rate", precision = 10, scale = 2)
  private BigDecimal hourlyRate;

  @Size(max = 50)
  @Column(name = "\"position\"", length = 50)
  private String position;

  @Size(max = 100)
  @Column(name = "specialization", length = 100)
  private String specialization;
}
