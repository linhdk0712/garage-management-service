package vn.utc.service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity
@Table(name = "user_roles")
public class UserRole {
  @SequenceGenerator(name = "user_roles_id_gen", sequenceName = "roles_id_seq", allocationSize = 1)
  @EmbeddedId
  private UserRoleId id;

  @MapsId("userId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @MapsId("roleId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "role_id", nullable = false)
  private Role role;

  public UserRole setId(UserRoleId id) {
    this.id = id;
    return this;
  }

  public UserRole setUser(User user) {
    this.user = user;
    return this;
  }

  public UserRole setRole(Role role) {
    this.role = role;
    return this;
  }
}
