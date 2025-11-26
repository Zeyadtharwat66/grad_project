package com.pos.grad_project.service;

import com.pos.grad_project.model.dto.LoginDTO;
import com.pos.grad_project.model.dto.RegisterDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;


public interface StudentService {
    public ResponseEntity<?> login(LoginDTO loginDTO);
    public ResponseEntity<?> register(RegisterDTO registerDTO);
    public ResponseEntity<?> logout(HttpServletResponse response);
    public ResponseEntity<?> getCurrentUser();
    public ResponseEntity<?> getStudent(Long id);
    public ResponseEntity<?> changePassword(Long id, String newPassword, String oldPassword);
    public ResponseEntity<?> changeUsername(Long id, String newUsername);
    public ResponseEntity<?> changeProfilePicture(Long id, String picture);
    public ResponseEntity<?> getMyCourses(Long id, int size, int page);
    public ResponseEntity<?> getMyCart(Long id, int size, int page);
    public ResponseEntity<?> getMyWishList(Long id, int size, int page);
    public ResponseEntity<?> addToMyWishList(Long courseId,Long studentId);
    public ResponseEntity<?> addToMyCart(Long courseId,Long studentId);
    public ResponseEntity<?> addToMyCourses(Long courseId,Long studentId);
    public ResponseEntity<?> deleteFromMyWishList(Long courseId,Long studentId);
    public ResponseEntity<?> deleteFromMyCart(Long courseId,Long studentId);
    public ResponseEntity<?> deleteFromMyCourses(Long courseId,Long studentId);

}
