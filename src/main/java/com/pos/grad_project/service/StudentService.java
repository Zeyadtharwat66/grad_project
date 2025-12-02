package com.pos.grad_project.service;

import com.pos.grad_project.model.dto.CheckoutRequestDTO;
import com.pos.grad_project.model.dto.CoursesCheckOutReqDTO;
import com.pos.grad_project.model.dto.LoginDTO;
import com.pos.grad_project.model.dto.RegisterDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;


public interface StudentService {
    public ResponseEntity<?> login(LoginDTO loginDTO);
    public ResponseEntity<?> register(RegisterDTO registerDTO);
    public ResponseEntity<?> logout();
    public ResponseEntity<?> getCurrentUser();
    public ResponseEntity<?> getStudent(Long id);
    public ResponseEntity<?> changePassword(Long id, String newPassword, String oldPassword);
    public ResponseEntity<?> changeUsername(Long id, String newUsername);
    public ResponseEntity<?> changeProfilePicture(Long id, String picture);
    public ResponseEntity<?> getMyCourses(int size, int page);
    public ResponseEntity<?> getMyCart(int size, int page);
    public ResponseEntity<?> getMyWishList(int size, int page);
    public ResponseEntity<?> addToMyWishList(Long courseId);
    public ResponseEntity<?> addToMyCart(Long courseId);
    public ResponseEntity<?> addToMyCourses(CheckoutRequestDTO course);
    public ResponseEntity<?> deleteFromMyWishList(Long courseId);
    public ResponseEntity<?> deleteFromMyCart(Long courseId);
    public ResponseEntity<?> deleteFromMyCourses(Long courseId);
    public ResponseEntity<?> deleteAllFromMyCart();
    public ResponseEntity<?> deleteAllFromMyWishList();

}
