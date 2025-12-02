package com.pos.grad_project.controller;
import com.pos.grad_project.model.dto.CheckoutRequestDTO;
import com.pos.grad_project.model.dto.CoursesCheckOutReqDTO;
import com.pos.grad_project.model.dto.CoursesReqDTO;
import com.pos.grad_project.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;
    @GetMapping("/profile/{id}")
    public ResponseEntity<?> getProfile(@PathVariable long id) {
        return this.studentService.getStudent(id);
    }
    @PutMapping("/change-password/{id}/{newPassword}/{oldPassword}")
    public ResponseEntity<?> changePassword(@PathVariable Long id, @PathVariable String newPassword, @PathVariable String oldPassword) {
        return this.studentService.changePassword(id, newPassword, oldPassword);
    }
    @PutMapping("/change-username/{id}/{newUsername}")
    public ResponseEntity<?> changeUsername(@PathVariable Long id,@PathVariable String newUsername) {
        return this.studentService.changeUsername(id, newUsername);
    }
    @PutMapping("/change-profile-picture/{id}/{profilePicture}")
    public ResponseEntity<?> changeProfilePicture(@PathVariable Long id,@PathVariable String profilePicture) {
        return this.studentService.changeProfilePicture(id, profilePicture);
    }
    @GetMapping("/get-my-courses/{size}/{page}")
    public ResponseEntity<?> getMyCourses(@PathVariable int size,@PathVariable int page) {
        return this.studentService.getMyCourses(size,page);
    }
    @GetMapping("/get-my-wishList/{size}/{page}")
    public ResponseEntity<?> getMyWishList(@PathVariable int size,@PathVariable int page) {
        return this.studentService.getMyWishList(size,page);
    }
    @GetMapping("/get-my-cart/{size}/{page}")
    public ResponseEntity<?> getMyCart(@PathVariable int size,@PathVariable int page) {
        return this.studentService.getMyCart(size,page);
    }
    @PostMapping("/add-my-cart/{courseId}")
    public ResponseEntity<?> addMyCart(@PathVariable Long courseId) {
        return this.studentService.addToMyCart(courseId);
    }
    @PostMapping("/add-my-wishList/{courseId}")
    public ResponseEntity<?> addMyWishList(@PathVariable Long courseId) {
        return this.studentService.addToMyWishList(courseId);
    }
    @PostMapping("/checkout")
    public ResponseEntity<?> addMyCourse(@RequestBody CheckoutRequestDTO request) {
        return this.studentService.addToMyCourses(request);
    }
    @DeleteMapping("/delete-my-cart/{courseId}")
    public ResponseEntity<?> deleteFromCart(@PathVariable Long courseId) {
        return this.studentService.deleteFromMyCart(courseId);
    }
    @DeleteMapping("/delete-my-wishList/{courseId}")
    public ResponseEntity<?> deleteFromWishList(@PathVariable Long courseId) {
        return this.studentService.deleteFromMyWishList(courseId);
    }
    @DeleteMapping("/delete-my-course/{courseId}")
    public ResponseEntity<?> deleteFromCourse(@PathVariable Long courseId) {
        return this.studentService.deleteFromMyCourses(courseId);
    }
    @DeleteMapping("/delete-all-from-my-cart")
    public ResponseEntity<?> deleteAllFromMyCart() {
        return this.studentService.deleteAllFromMyCart();
    }
    @DeleteMapping("/delete-all-from-my-wishlist")
    public ResponseEntity<?> deleteAllFromMyWishList() {
        return this.studentService.deleteAllFromMyWishList();
    }
}
