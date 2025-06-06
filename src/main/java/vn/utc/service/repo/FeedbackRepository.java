package vn.utc.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.utc.service.entity.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {}
