package vn.utc.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.utc.service.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {}
