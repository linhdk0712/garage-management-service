package vn.utc.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.utc.service.entity.SparePart;

public interface SparePartRepository extends JpaRepository<SparePart, Integer> {}
