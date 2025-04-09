package com.example.exambackend.controllers;


import com.example.exambackend.dal.services.AttemptService;
import com.example.exambackend.models.Attempt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/attempts")
public class AttemptController {
    private final AttemptService attemptService;

    public AttemptController (AttemptService attemptService) {
        this.attemptService = attemptService;
    }

    // Create attempt
    @PostMapping
    public ResponseEntity<Attempt> createAttempt(@RequestBody Attempt attempt) {
        Attempt createdAttempt = attemptService.createAttempt(attempt);
        return ResponseEntity.ok(createdAttempt);
    }

    // Get all attempts
    @GetMapping
    public ResponseEntity<List<Attempt>> getAllAttempts() {
        return ResponseEntity.ok(attemptService.getAllAttempts());
    }

    // Get attempt by id
    @GetMapping("/{id}")
    public ResponseEntity<Attempt> getAttemptById(@PathVariable Long id) {
        Optional<Attempt> attemptOpt = attemptService.getAttemptById(id);
        return attemptOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get attempts by user id
    @GetMapping(path = "/user/{userId}")
    public ResponseEntity<List<Attempt>> getAttemptsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(attemptService.getAllAttemptsByUserId(userId));
    }

    // Update attempt
    @PutMapping(path = "/{id}")
    public ResponseEntity<Attempt> updateAttempts(@PathVariable Long id, @RequestBody Attempt attempt) {
        attempt.setId(id);
        try {
            return ResponseEntity.ok(attemptService.updateAttempt(attempt));
        } catch (IllegalArgumentException e ) {
            return ResponseEntity.notFound().build();
        }
    }

}
