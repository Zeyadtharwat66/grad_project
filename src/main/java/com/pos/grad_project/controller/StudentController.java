package com.pos.grad_project.controller;

import com.pos.grad_project.model.dto.ChangePasswordRequest;
import com.pos.grad_project.model.dto.ChangeProfilePictureRequest;
import com.pos.grad_project.model.dto.ChangeUsernameRequest;
import com.pos.grad_project.model.dto.CheckoutRequestDTO;
import com.pos.grad_project.model.dto.CommentReqDTO;
import com.pos.grad_project.service.StudentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/student")
@Validated
public class StudentController {
    private final StudentService studentService;

    @GetMapping("/profile/{id}")
    public ResponseEntity<?> getProfile(@PathVariable long id) {
        return studentService.getStudent(id);
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        return studentService.changePassword(request);
    }

    @PutMapping("/change-username")
    public ResponseEntity<?> changeUsername(@Valid @RequestBody ChangeUsernameRequest request) {
        return studentService.changeUsername(request);
    }

    @PutMapping("/change-profile-picture")
    public ResponseEntity<?> changeProfilePicture(@Valid @RequestBody ChangeProfilePictureRequest request) {
        return studentService.changeProfilePicture(request);
    }

    @GetMapping("/get-my-courses/{size}/{page}")
    public ResponseEntity<?> getMyCourses(@PathVariable int size, @PathVariable int page) {
        return studentService.getMyCourses(size, page);
    }

    @GetMapping("/get-my-wishList/{size}/{page}")
    public ResponseEntity<?> getMyWishList(@PathVariable int size, @PathVariable int page) {
        return studentService.getMyWishList(size, page);
    }

    @GetMapping("/get-my-cart/{size}/{page}")
    public ResponseEntity<?> getMyCart(@PathVariable int size, @PathVariable int page) {
        return studentService.getMyCart(size, page);
    }

    @PostMapping("/add-my-cart/{courseId}")
    public ResponseEntity<?> addMyCart(@PathVariable Long courseId) {
        return studentService.addToMyCart(courseId);
    }

    @PostMapping("/add-my-wishList/{courseId}")
    public ResponseEntity<?> addMyWishList(@PathVariable Long courseId) {
        return studentService.addToMyWishList(courseId);
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> addMyCourse(@Valid @RequestBody CheckoutRequestDTO request) {
        return studentService.addToMyCourses(request);
    }

    @DeleteMapping("/delete-my-cart/{courseId}")
    public ResponseEntity<?> deleteFromCart(@PathVariable Long courseId) {
        return studentService.deleteFromMyCart(courseId);
    }

    @DeleteMapping("/delete-my-wishList/{courseId}")
    public ResponseEntity<?> deleteFromWishList(@PathVariable Long courseId) {
        return studentService.deleteFromMyWishList(courseId);
    }

    @DeleteMapping("/delete-my-course/{courseId}")
    public ResponseEntity<?> deleteFromCourse(@PathVariable Long courseId) {
        return studentService.deleteFromMyCourses(courseId);
    }

    @DeleteMapping("/delete-all-from-my-cart")
    public ResponseEntity<?> deleteAllFromMyCart() {
        return studentService.deleteAllFromMyCart();
    }

    @DeleteMapping("/delete-all-from-my-wishlist")
    public ResponseEntity<?> deleteAllFromMyWishList() {
        return studentService.deleteAllFromMyWishList();
    }

    @PostMapping("/add-comment")
    public ResponseEntity<?> addComment(@Valid @RequestBody CommentReqDTO commentReqDTO) {
        return studentService.addComment(commentReqDTO);
    }

    @GetMapping("/get-comment/{videoId}")
    public ResponseEntity<?> getComment(@PathVariable Long videoId) {
        return studentService.getComments(videoId);
    }
}
