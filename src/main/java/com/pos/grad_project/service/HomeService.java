package com.pos.grad_project.service;

import org.springframework.http.ResponseEntity;

public interface HomeService {
    public ResponseEntity<?> show4Feedback();
    public ResponseEntity<?> top2Teachers();
    public ResponseEntity<?> top6Courses();
    public ResponseEntity<?> statistics();


}
