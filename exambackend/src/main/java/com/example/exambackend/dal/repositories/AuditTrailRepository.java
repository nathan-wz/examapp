package com.example.exambackend.dal.repositories;

import com.example.exambackend.models.AuditTrail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditTrailRepository extends JpaRepository<AuditTrail, Long> {
    List<AuditTrail> findByUserIdOrderByDateTimeDesc(Long userId);
}
