package com.pos.grad_project.service.Imp;
import com.pos.grad_project.model.dto.LoginDTO;
import com.pos.grad_project.model.dto.MyCoursesItemRespDTO;
import com.pos.grad_project.model.dto.RegisterDTO;
import com.pos.grad_project.model.dto.StudentRespDTO;
import com.pos.grad_project.model.entity.*;
import com.pos.grad_project.model.mapper.StudentMapper;
import com.pos.grad_project.repository.*;
import com.pos.grad_project.service.JWTTokenService;
import com.pos.grad_project.service.StudentService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StudentServiceImp implements StudentService {
    private final JWTTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;
    private final StudentRepo studentRepo;
    private final PasswordEncoder passwordEncoder;
    private final StudentMapper studentMapper;
    private final MyCoursesItemRepo myCoursesItemRepo;
    private final MyCartItemRepo myCartItemRepo;
    private final MyWishListItemsRepo myWishListItemsRepo;
    private final CourseRepo courseRepo;
    private final CartRepo cartRepo;
    private final WishListRepo wishListRepo;
    private final MyCourseRepo myCourseRepo;
    @Override
    public ResponseEntity<?> login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password())
        );
        String token = jwtTokenService.generateJWTToken(authentication);
        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("None")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("message", loginDTO));
    }
    @Override
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok(Map.of(
                    "username", authentication.getName(),
                    "roles", authentication.getAuthorities()
            ));
        }
        return ResponseEntity.status(497).body("Not logged in");
    }
    @Override
    public ResponseEntity<?> register(RegisterDTO registerDTO) {
        if (studentRepo.findByUsername(registerDTO.username()).isPresent()) {
            throw new RuntimeException("Username already exists!");
        }
        StudentEntity studentEntity = StudentEntity.builder()
                .username(registerDTO.username())
                .password(passwordEncoder.encode(registerDTO.password()))
                .role(registerDTO.role() != null ? registerDTO.role() : "STUDENT")
                .birthDate(registerDTO.birthDate())
                .email(registerDTO.email())
                .grade(registerDTO.grade())
                .gender(registerDTO.gender())
                .phoneNumber(registerDTO.phoneNumber())
                .build();
        studentRepo.save(studentEntity);
        StudentMyCourseEntity myCourse=StudentMyCourseEntity.builder()
                .coursesCount(0)
                .student(studentEntity)
                .createdAt(LocalDateTime.now())
                .build();
        this.myCourseRepo.save(myCourse);
        WishListEntity wishList=WishListEntity.builder()
                .student(studentEntity)
                .createdAt(LocalDateTime.now())
                .build();
        this.wishListRepo.save(wishList);
        CartEntity cart=CartEntity.builder()
                .student(studentEntity)
                .totalPrice(0.0)
                .createdAt(LocalDateTime.now())
                .build();
        this.cartRepo.save(cart);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registerDTO.username(), registerDTO.password())
        );
        String token = jwtTokenService.generateJWTToken(authentication);
        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("None")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("message", registerDTO));
    }
    @Override
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
    @Override
    public ResponseEntity<?> getStudent(Long id) {
        StudentEntity student = studentRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
        StudentRespDTO res=this.studentMapper.toRespDTO(student);
        return ResponseEntity.ok(res);
    }
    @Override
    public ResponseEntity<?> changePassword(Long id, String newPassword, String oldPassword) {
        StudentEntity student = this.studentRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
        if(!passwordEncoder.matches(oldPassword,student.getPassword())){
            return ResponseEntity.ok("Password does not match!");
        }
        student.setPassword(passwordEncoder.encode(newPassword));
        student.setUpdatedAt(LocalDateTime.now());
        this.studentRepo.save(student);
        return ResponseEntity.ok("Password changed!");
    }
    @Override
    public ResponseEntity<?> changeUsername(Long id, String newUsername) {
        StudentEntity student = this.studentRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
        if(this.studentRepo.findByUsername(newUsername).isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }
        student.setUsername(newUsername);
        student.setUpdatedAt(LocalDateTime.now());
        this.studentRepo.save(student);
        return ResponseEntity.ok("Username changed!");
    }
    @Override
    public ResponseEntity<?> changeProfilePicture(Long id, String picture) {
        StudentEntity student = this.studentRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
        student.setUpdatedAt(LocalDateTime.now());
        student.setProfilePictureUrl(picture);
        this.studentRepo.save(student);
        return ResponseEntity.ok("Profile picture changed!");
    }
    @Override
    public ResponseEntity<?> getMyCourses(Long id, int size, int page) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("courseId").ascending());
        Page<StudentMyCourseItemEntity> items=this.myCoursesItemRepo.findAllByStudentId(id,pageable);
        if(items.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No courses found!");
        }
        Page<Map<String,Object>> myCourses=items.map(
                s->{
                    Map<String,Object> map=new HashMap<>();
                    map.put("course name",s.getCourse().getName());
                    map.put("course progress",s.getProgress());
                    map.put("course teacher",s.getCourse().getTeacher().getUsername());
                    map.put("course picture",s.getCourse().getImageUrl());
                    return map;
                });
        return ResponseEntity.ok(myCourses);
    }
    @Override
    public ResponseEntity<?> getMyCart(Long id, int size, int page) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("courseId").ascending());
        Page<CartItemEntity> items=this.myCartItemRepo.findAllByStudentId(id,pageable);
        if(items.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No courses found!");
        }
        Page<Map<String,Object>> cartItems=items.map(
                s->{
                    Map<String,Object> map=new HashMap<>();
                    map.put("course name",s.getCourse().getName());
                    map.put("price",s.getCourse().getPrice());
                    map.put("rate",s.getCourse().getRating());
                    map.put("duration",s.getCourse().getDuration());
                    map.put("course teacher",s.getCourse().getTeacher().getUsername());
                    map.put("course picture",s.getCourse().getImageUrl());
                    return map;
                });
        double totalPrice=items.stream().map(i->i.getCourse().getPrice()).mapToDouble(Double::doubleValue).sum();
        long totalCourses=items.stream().count();
        Map<String, Object> response = new HashMap<>();
        response.put("items", cartItems);
        response.put("totalPrice", totalPrice);
        response.put("totalcourses", totalCourses);
        return ResponseEntity.ok(response);
    }
    @Override
    public ResponseEntity<?> getMyWishList(Long id, int size, int page) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("courseId").ascending());
        Page<WishlistItemEntity> items=this.myWishListItemsRepo.findAllByStudentId(id,pageable);
        if(items.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No courses found!");
        }
        Page<Map<String,Object>> wishListItems=items.map(
                s->{
                    Map<String,Object> map=new HashMap<>();
                    map.put("course name",s.getCourse().getName());
                    map.put("price",s.getCourse().getPrice());
                    map.put("rate",s.getCourse().getRating());
                    map.put("duration",s.getCourse().getDuration());
                    map.put("course teacher",s.getCourse().getTeacher().getUsername());
                    map.put("course picture",s.getCourse().getImageUrl());
                    return map;
                });
        return ResponseEntity.ok(wishListItems);
    }
    @Override
    public ResponseEntity<?> addToMyCart(Long courseId,Long studentId) {
        CourseEntity course=this.courseRepo.findById(courseId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        StudentEntity student=this.studentRepo.findById(studentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
        CartEntity cart=this.cartRepo.findByStudent(student);
        if(this.myCartItemRepo.existsByCourse(course)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Course already exists");
        }
        cart.setTotalPrice(cart.getTotalPrice()+course.getPrice());
        CartItemEntity item= CartItemEntity.builder()
                .student(student)
                .course(course)
                .price(course.getPrice())
                .createdAt(LocalDateTime.now())
                .cart(cart)
                .build();
        cart.getCartItems().add(item);
        this.cartRepo.save(cart);
        return ResponseEntity.ok("Added course to cart");
    }
    @Override
    public ResponseEntity<?> addToMyWishList(Long courseId,Long studentId) {
        CourseEntity course=this.courseRepo.findById(courseId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        StudentEntity student=this.studentRepo.findById(studentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
        WishListEntity wishList=this.wishListRepo.findByStudent(student);
        if(this.myWishListItemsRepo.existsByCourse(course)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Course already exists");
        }
        WishlistItemEntity item= WishlistItemEntity.builder()
                .student(student)
                .course(course)
                .createdAt(LocalDateTime.now())
                .wishlist(wishList)
                .build();
        wishList.getWishlistItem().add(item);
        this.wishListRepo.save(wishList);
        return ResponseEntity.ok("Added course to wishList");
    }
    @Override
    public ResponseEntity<?> addToMyCourses(Long courseId,Long studentId) {
        CourseEntity course=this.courseRepo.findById(courseId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        StudentEntity student=this.studentRepo.findById(studentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
        StudentMyCourseEntity myCourse=this.myCourseRepo.findByStudent(student);
        if(this.myCoursesItemRepo.existsByCourse(course)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Course already exists");
        }
        myCourse.setCoursesCount(myCourse.getCoursesCount()+1);
        StudentMyCourseItemEntity item= StudentMyCourseItemEntity.builder()
                .student(student)
                .course(course)
                .createdAt(LocalDateTime.now())
                .studentMyCourse(myCourse)
                .progress(0.0)
                .isCompleted(false)
                .build();
        myCourse.getMyCourseItems().add(item);
        this.myCourseRepo.save(myCourse);
        return ResponseEntity.ok("Added course to cart" );
    }
    @Override
    public ResponseEntity<?> deleteFromMyWishList(Long courseId, Long studentId) {
        CourseEntity course=this.courseRepo.findById(courseId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        StudentEntity student=this.studentRepo.findById(studentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
        WishListEntity wishList=this.wishListRepo.findByStudent(student);
        if(!this.myWishListItemsRepo.existsByCourse(course)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Course doesnot exist");
        }
        WishlistItemEntity item =this.myWishListItemsRepo.findByCourse(course);
        item.setDeletedAt(LocalDateTime.now());
        wishList.getWishlistItem().remove(item);
        this.wishListRepo.save(wishList);
        return ResponseEntity.ok("Removed course from wishList");
    }
    @Override
    public ResponseEntity<?> deleteFromMyCart(Long courseId, Long studentId) {
        CourseEntity course=this.courseRepo.findById(courseId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        StudentEntity student=this.studentRepo.findById(studentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
        CartEntity cart=this.cartRepo.findByStudent(student);
        if(!this.myCartItemRepo.existsByCourse(course)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Course doesnot exist");
        }
        CartItemEntity item =this.myCartItemRepo.findByCourse(course);
        item.setDeletedAt(LocalDateTime.now());
        cart.getCartItems().remove(item);
        cart.setTotalPrice(cart.getTotalPrice()-course.getPrice());
        this.cartRepo.save(cart);
        return ResponseEntity.ok("Removed course from cart");
    }
    @Override
    public ResponseEntity<?> deleteFromMyCourses(Long courseId, Long studentId) {
        CourseEntity course=this.courseRepo.findById(courseId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        StudentEntity student=this.studentRepo.findById(studentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
        StudentMyCourseEntity myCourse=this.myCourseRepo.findByStudent(student);
        if(!this.myCoursesItemRepo.existsByCourse(course)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Course doesnot exist");
        }
        StudentMyCourseItemEntity item =this.myCoursesItemRepo.findByCourse(course);
        item.setDeletedAt(LocalDateTime.now());
        myCourse.getMyCourseItems().remove(item);
        myCourse.setCoursesCount(myCourse.getCoursesCount()-1);
        this.myCourseRepo.save(myCourse);
        return ResponseEntity.ok("Removed course from myCourses");
    }
}
