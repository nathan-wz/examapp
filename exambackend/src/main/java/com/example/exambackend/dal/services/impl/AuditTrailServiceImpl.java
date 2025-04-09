package com.example.exambackend.dal.services.impl;

import com.example.exambackend.dal.repositories.AuditTrailRepository;
import com.example.exambackend.dal.services.AuditTrailService;
import com.example.exambackend.models.AuditTrail;
import com.example.exambackend.models.User;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class AuditTrailServiceImpl implements AuditTrailService {
    private final AuditTrailRepository auditTrailRepository;

    public AuditTrailServiceImpl(AuditTrailRepository auditTrailRepository) {
        this.auditTrailRepository = auditTrailRepository;
    }

    @Override
    public AuditTrail createAuditTrail(AuditTrail auditTrail) {
        return auditTrailRepository.save(auditTrail);
    }

    @Override
    public Optional<AuditTrail> getAuditTrailById(Long id){
        return auditTrailRepository.findById(id);
    }

    @Override
    public List<AuditTrail> getAuditTrailsByUserId(Long userId){
        return auditTrailRepository.findByUserIdOrderByDateTimeDesc(userId);
    }

    @Override
    public List<AuditTrail> getAllAuditTrails() {
        return auditTrailRepository.findAll();
    }

    @Override
    public void logAction(User user, String action) {
        AuditTrail audit = new AuditTrail();

        audit.setUser(user);
        audit.setAction(action);
        audit.setDateTime(new Timestamp(System.currentTimeMillis()));
        auditTrailRepository.save(audit);
    }
}
