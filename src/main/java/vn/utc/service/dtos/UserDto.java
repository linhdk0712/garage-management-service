package vn.utc.service.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

/** DTO for {@link vn.utc.service.entity.User} */
public class UserDto implements Serializable {
  private Integer id;

  @NotNull
  @Size(max = 50)
  private String username;

  @NotNull
  @Size(max = 255)
  private String password;

  @NotNull
  @Size(max = 100)
  private String email;

  private String role;

  @Size(max = 20)
  private String phone;

  @NotNull
  @Size(max = 20)
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
