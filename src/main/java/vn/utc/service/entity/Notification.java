package vn.utc.service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Entity
@Table(name = "notifications")
public class Notification {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notifications_id_gen")
  @SequenceGenerator(
      name = "notifications_id_gen",
      sequenceName = "notifications_notification_id_seq",
      allocationSize = 1)
  @Column(name = "notification_id", nullable = false)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private vn.utc.service.entity.User user;

  @Size(max = 100)
  @NotNull
  @Column(name = "title", nullable = false, length = 100)
  private String title;

  @NotNull
  @Column(name = "message", nullable = false, length = Integer.MAX_VALUE)
  private String message;

  @Size(max = 50)
  @Column(name = "type", length = 50)
  private String type;

  @ColumnDefault("false")
  @Column(name = "is_read")
  private Boolean isRead;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "created_at")
  private Instant createdAt;

    public Notification setId(Integer id) {
        this.id = id;
        return this;
    }

    public Notification setUser(User user) {
        this.user = user;
        return this;
    }

    public Notification setTitle(String title) {
        this.title = title;
        return this;
    }

    public Notification setMessage(String message) {
        this.message = message;
        return this;
    }

    public Notification setType(String type) {
        this.type = type;
        return this;
    }

    public Notification setRead(Boolean read) {
        isRead = read;
        return this;
    }

    public Notification setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
