package com.example.exambackend.dal.services;

import com.example.exambackend.models.AuditTrail;
import com.example.exambackend.models.User;

import java.util.List;
import java.util.Optional;

public interface AuditTrailService {
    AuditTrail createAuditTrail(AuditTrail auditTrail);
    Optional<AuditTrail> getAuditTrailById(Long id);
    List<AuditTrail> getAuditTrailsByUserId(Long userId);
    List<AuditTrail> getAllAuditTrails();
    void logAction(User user, String action);
}
