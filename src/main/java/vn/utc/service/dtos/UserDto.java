package vn.utc.service.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

/** DTO for {@link vn.utc.service.entity.User} */
public class UserDto implements Serializable {
  private Integer id;

  @NotBlank(message = "Username is required")
  @Size(max = 50, message = "Username must not exceed 50 characters")
  private String username;

  @NotBlank(message = "Password is required")
  @Size(max = 255, message = "Password must not exceed 255 characters")
  private String password;

  @NotBlank(message = "Email is required")
  @Email(message = "Email should be valid")
  @Size(max = 100, message = "Email must not exceed 100 characters")
  private String email;

  @Size(max = 20, message = "Role must not exceed 20 characters")
  private String role;

  @Size(max = 20, message = "Phone must not exceed 20 characters")
  @Pattern(regexp = "^[+]?[0-9\\s\\-\\(\\)]+$", message = "Phone number should contain only digits, spaces, hyphens, and parentheses")
  private String phone;

  @NotNull(message = "Roles are required")
  @Size(max = 20, message = "Roles must not exceed 20 characters")
  private Set<RoleDto> roles;

  private Instant createdAt;
  private Instant lastLogin;
  private Boolean isActive;

  public Integer getId() {
    return id;
  }

  public UserDto setId(Integer id) {
    this.id = id;
    return this;
  }

  public String getUsername() {
    return username;
  }

  public UserDto setUsername(String username) {
    this.username = username;
    return this;
  }

  public String getPassword() {
    return password;
  }

  public UserDto setPassword(String password) {
    this.password = password;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public UserDto setEmail(String email) {
    this.email = email;
    return this;
  }

  public String getPhone() {
    return phone;
  }

  public UserDto setPhone(String phone) {
    this.phone = phone;
    return this;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public UserDto setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public Instant getLastLogin() {
    return lastLogin;
  }

  public UserDto setLastLogin(Instant lastLogin) {
    this.lastLogin = lastLogin;
    return this;
  }

  public Boolean getActive() {
    return isActive;
  }

  public UserDto setActive(Boolean active) {
    isActive = active;
    return this;
  }

  public String getRole() {
    return role;
  }

  public UserDto setRole(String role) {
    this.role = role;
    return this;
  }

  public Set<RoleDto> getRoles() {
    return roles;
  }

  public UserDto setRoles(Set<RoleDto> roles) {
    this.roles = roles;
    return this;
  }
}
