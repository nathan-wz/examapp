package com.example.exambackend.dal.services;

import com.example.exambackend.models.Session;

import java.util.List;

public interface SessionService {
    Session createSession(Session session);
    List<Session> getSessionsByUserId(Long userId);
    void deleteSession(Long id);
}
