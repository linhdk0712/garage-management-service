package vn.utc.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.utc.service.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {}
