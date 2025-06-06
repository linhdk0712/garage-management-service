package vn.utc.service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

@Getter

@Entity
@Table(name = "feedback")
public class Feedback {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "feedback_id_gen")
  @SequenceGenerator(
      name = "feedback_id_gen",
      sequenceName = "feedback_feedback_id_seq",
      allocationSize = 1)
  @Column(name = "feedback_id", nullable = false)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "work_order_id")
  private vn.utc.service.entity.WorkOrder workOrder;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id")
  private Customer customer;

  @NotNull
  @Column(name = "rating", nullable = false)
  private Integer rating;

  @Column(name = "comments", length = Integer.MAX_VALUE)
  private String comments;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "submitted_at")
  private Instant submittedAt;

    public Feedback setId(Integer id) {
        this.id = id;
        return this;
    }

    public Feedback setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
        return this;
    }

    public Feedback setCustomer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public Feedback setRating(Integer rating) {
        this.rating = rating;
        return this;
    }

    public Feedback setComments(String comments) {
        this.comments = comments;
        return this;
    }

    public Feedback setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
        return this;
    }
}
