package com.pos.grad_project.controller;
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
    @GetMapping("/get-my-courses/{id}/{size}/{page}")
    public ResponseEntity<?> getMyCourses(@PathVariable long id,@PathVariable int size,@PathVariable int page) {
        return this.studentService.getMyCourses(id,size,page);
    }
    @GetMapping("/get-my-wishList/{id}/{size}/{page}")
    public ResponseEntity<?> getMyWishList(@PathVariable long id,@PathVariable int size,@PathVariable int page) {
        return this.studentService.getMyWishList(id,size,page);
    }
    @GetMapping("/get-my-cart/{id}/{size}/{page}")
    public ResponseEntity<?> getMyCart(@PathVariable long id,@PathVariable int size,@PathVariable int page) {
        return this.studentService.getMyCart(id,size,page);
    }
    @PostMapping("/add-my-cart/{courseId}/{studentId}")
    public ResponseEntity<?> addMyCart(@PathVariable Long courseId,@PathVariable Long studentId) {
        return this.studentService.addToMyCart(courseId, studentId);
    }
    @PostMapping("/add-my-wishList/{courseId}/{studentId}")
    public ResponseEntity<?> addMyWishList(@PathVariable Long courseId,@PathVariable Long studentId) {
        return this.studentService.addToMyWishList(courseId, studentId);
    }
    @PostMapping("/add-my-course/{courseId}/{studentId}")
    public ResponseEntity<?> addMyCourse(@PathVariable Long courseId,@PathVariable Long studentId) {
        return this.studentService.addToMyCourses(courseId, studentId);
    }
    @DeleteMapping("/delete-my-cart/{courseId}/{studentId}")
    public ResponseEntity<?> deleteFromCart(@PathVariable Long courseId,@PathVariable Long studentId) {
        return this.studentService.deleteFromMyCart(courseId, studentId);
    }
    @DeleteMapping("/delete-my-wishList/{courseId}/{studentId}")
    public ResponseEntity<?> deleteFromWishList(@PathVariable Long courseId,@PathVariable Long studentId) {
        return this.studentService.deleteFromMyWishList(courseId, studentId);
    }
    @DeleteMapping("/delete-my-course/{courseId}/{studentId}")
    public ResponseEntity<?> deleteFromCourse(@PathVariable Long courseId,@PathVariable Long studentId) {
        return this.studentService.deleteFromMyCourses(courseId, studentId);
    }
}
