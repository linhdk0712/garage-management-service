package vn.utc.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

@Getter
@Embeddable
public class UserRoleId implements Serializable {
  private static final long serialVersionUID = -8109379588411262024L;

  @NotNull
  @Column(name = "user_id", nullable = false)
  private Long userId;

  @NotNull
  @Column(name = "role_id", nullable = false)
  private Long roleId;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    UserRoleId entity = (UserRoleId) o;
    return Objects.equals(this.roleId, entity.roleId) && Objects.equals(this.userId, entity.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(roleId, userId);
  }

  public UserRoleId setUserId(Long userId) {
    this.userId = userId;
    return this;
  }

  public UserRoleId setRoleId(Long roleId) {
    this.roleId = roleId;
    return this;
  }
}
