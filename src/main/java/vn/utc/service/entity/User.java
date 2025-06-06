package vn.utc.service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_gen")
  @SequenceGenerator(name = "users_id_gen", sequenceName = "users_user_id_seq", allocationSize = 1)
  @Column(name = "user_id", nullable = false)
  private Integer id;

  @Size(max = 50)
  @NotNull
  @Column(name = "username", nullable = false, length = 50)
  private String username;

  @Size(max = 255)
  @NotNull
  @Column(name = "password", nullable = false)
  private String password;

  @Size(max = 100)
  @NotNull
  @Column(name = "email", nullable = false, length = 100)
  private String email;

  @Size(max = 20)
  @Column(name = "phone", length = 20)
  private String phone;

  @Size(max = 20)
  @NotNull
  @Column(name = "role", nullable = false, length = 20)
  private String role;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "created_at")
  private Instant createdAt;

  @Column(name = "last_login")
  private Instant lastLogin;

  @ColumnDefault("true")
  @Column(name = "is_active")
  private Boolean isActive;

  @OneToOne(mappedBy = "user")
  private Customer customer;

  @OneToMany(mappedBy = "user")
  private Set<Notification> notifications = new LinkedHashSet<>();

  @OneToOne(mappedBy = "user")
  private Staff staff;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  public User setId(Integer id) {
    this.id = id;
    return this;
  }

  public User setUsername(String username) {
    this.username = username;
    return this;
  }

  public User setPassword(String password) {
    this.password = password;
    return this;
  }

  public User setEmail(String email) {
    this.email = email;
    return this;
  }

  public User setPhone(String phone) {
    this.phone = phone;
    return this;
  }

  public User setRole(String role) {
    this.role = role;
    return this;
  }

  public User setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public User setLastLogin(Instant lastLogin) {
    this.lastLogin = lastLogin;
    return this;
  }

  public User setActive(Boolean active) {
    isActive = active;
    return this;
  }

  public User setCustomer(Customer customer) {
    this.customer = customer;
    return this;
  }

  public User setNotifications(Set<Notification> notifications) {
    this.notifications = notifications;
    return this;
  }

  public User setStaff(Staff staff) {
    this.staff = staff;
    return this;
  }

  public User setRoles(Set<Role> roles) {
    this.roles = roles;
    return this;
  }

  public Integer getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getEmail() {
    return email;
  }

  public String getPhone() {
    return phone;
  }

  public String getRole() {
    return role;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getLastLogin() {
    return lastLogin;
  }

  public Boolean getActive() {
    return isActive != null ? isActive : Boolean.TRUE;
  }

  public Customer getCustomer() {
    return customer;
  }

  public Set<Notification> getNotifications() {
    return notifications;
  }

  public Staff getStaff() {
    return staff;
  }

  public Set<Role> getRoles() {
    return roles;
  }
}
