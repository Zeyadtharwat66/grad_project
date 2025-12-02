package com.pos.grad_project.service.Imp;
import com.pos.grad_project.model.dto.*;
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
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.*;
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
    private final CourseProgressRepo courseProgressRepo;
    @Override
    public ResponseEntity<?> login(LoginDTO loginDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password())
        );

        String token = jwtTokenService.generateJWTToken(authentication);

        StudentEntity studentEntity = studentRepo.findByUsername(loginDTO.username())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        HashMap<String,Object> user = new HashMap<>();
        user.put("id", studentEntity.getId());
        user.put("username", studentEntity.getUsername());
        user.put("grade", studentEntity.getGrade());
        user.put("phoneNumber", studentEntity.getPhoneNumber());
        user.put("email", studentEntity.getEmail());
        user.put("birthDate", studentEntity.getBirthDate());
        user.put("gender", studentEntity.getGender());

        return ResponseEntity.ok(Map.of(
                "token", token,
                "user", user
        ));
    }
    @Override
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {

            return ResponseEntity.status(401).body("Not logged in");
        }
        // extract user details
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(Map.of(
                "username", userDetails.getUsername(),
                "roles", userDetails.getAuthorities()
        ));
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
                .myCourseItems(null)
                .createdAt(LocalDateTime.now())
                .build();
        this.myCourseRepo.save(myCourse);
        WishListEntity wishList=WishListEntity.builder()
                .student(studentEntity)
                .wishlistItem(null)
                .createdAt(LocalDateTime.now())
                .build();
        this.wishListRepo.save(wishList);
        CartEntity cart=CartEntity.builder()
                .student(studentEntity)
                .totalPrice(0.0)
                .cartItems(null)
                .createdAt(LocalDateTime.now())
                .build();
        this.cartRepo.save(cart);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registerDTO.username(), registerDTO.password())
        );
        String token = jwtTokenService.generateJWTToken(authentication);
        HashMap<String,Object> data=new HashMap<>();
        data.put("Id",studentEntity.getId());
        data.put("Username",studentEntity.getUsername());
        data.put("Grade",studentEntity.getGrade());
        data.put("PhoneNumber",studentEntity.getPhoneNumber());
        data.put("Email",studentEntity.getEmail());
        data.put("BirthDate",studentEntity.getBirthDate());
        data.put("Gender",studentEntity.getGender());
        return ResponseEntity.ok(Map.of("token",token,"user",data));

    }
    @Override
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of(
                "message", "Logged out successfully"
        ));
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
    public ResponseEntity<?> getMyCourses(int size, int page) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() ||
                auth.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }
        String username = auth.getName();
        StudentEntity student = studentRepo.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        Long id = student.getId();
        Pageable pageable = PageRequest.of(page, size, Sort.by("courseId").ascending());
        Page<StudentMyCourseItemEntity> items=this.myCoursesItemRepo.findAllByStudentId(id,pageable);
        if(items.isEmpty()){
            Map<String,Object> emptyResponse = new HashMap<>();
            emptyResponse.put("items", Collections.emptyList());
            emptyResponse.put("totalPrice", 0);
            emptyResponse.put("totalcourses", 0);
            emptyResponse.put("totalElements", 0);
            emptyResponse.put("totalPages", 0);
            emptyResponse.put("numberOfElements", 0);
            emptyResponse.put("size", items.getSize());
            emptyResponse.put("number", items.getNumber());
            return ResponseEntity.ok(emptyResponse);
        }
        Page<Map<String,Object>> myCourses=items.map(
                s->{
                    Map<String,Object> map=new HashMap<>();
                    map.put("coursename",s.getCourse().getName());
                    map.put("id",s.getCourse().getId());
                    map.put("courseprogress",s.getProgress());
                    map.put("courserating",s.getCourse().getRating());
                    map.put("totalNumberOfStudents",s.getCourse().getNumberOfStudents());
                    map.put("description",s.getCourse().getDescription());
                    map.put("coursepicture",s.getCourse().getImageUrl());
                    return map;
                });
        return ResponseEntity.ok(myCourses);
    }
    @Override
    public ResponseEntity<?> getMyCart(int size, int page) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() ||
                auth.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }
        String username = auth.getName();
        StudentEntity student = studentRepo.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        Pageable pageable = PageRequest.of(page, size, Sort.by("courseId").ascending());
        Page<CartItemEntity> items=this.myCartItemRepo.findAllByStudentId(student.getId(),pageable);
        if(items.isEmpty()){
            Map<String,Object> emptyResponse = new HashMap<>();
            emptyResponse.put("items", Collections.emptyList());
            emptyResponse.put("totalPrice", 0);
            emptyResponse.put("totalcourses", 0);
            emptyResponse.put("totalElements", 0);
            emptyResponse.put("totalPages", 0);
            emptyResponse.put("numberOfElements", 0);
            emptyResponse.put("size", items.getSize());
            emptyResponse.put("number", items.getNumber());
            return ResponseEntity.ok(emptyResponse);
        }
        Page<Map<String,Object>> cartItems=items.map(
                s->{
                    Map<String,Object> map=new HashMap<>();
                    map.put("coursename",s.getCourse().getName());
                    map.put("price",s.getCourse().getPrice());
                    map.put("rate",s.getCourse().getRating());
                    map.put("duration",s.getCourse().getDuration());
                    map.put("totalNumberOfStudents",s.getCourse().getNumberOfStudents());
                    map.put("description",s.getCourse().getDescription());
                    map.put("id",s.getCourse().getId());
                    map.put("courseteacher",s.getCourse().getTeacher().getUsername());
                    map.put("coursepicture",s.getCourse().getImageUrl());
                    return map;
                });
        Double totalPrice = myCartItemRepo.getTotalCartPrice(student.getId());
        long totalCourses=items.stream().count();
        Map<String, Object> response = new HashMap<>();
        response.put("items", cartItems);
        response.put("totalPrice", totalPrice);
        response.put("totalcourses", totalCourses);
        response.put("totalElements", items.getTotalElements());
        response.put("totalPages", items.getTotalPages());
        response.put("numberOfElements", items.getNumberOfElements());
        response.put("size", items.getSize());
        response.put("number", items.getNumber());
        return ResponseEntity.ok(response);
    }
    @Override
    public ResponseEntity<?> getMyWishList(int size, int page) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() ||
                auth.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }
        String username = auth.getName();
        StudentEntity student = studentRepo.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        Pageable pageable = PageRequest.of(page, size, Sort.by("courseId").ascending());
        Page<WishlistItemEntity> items=this.myWishListItemsRepo.findAllByStudentId(student.getId(),pageable);
        if(items.isEmpty()){
            Map<String,Object> emptyResponse = new HashMap<>();
            emptyResponse.put("items", Collections.emptyList());
            emptyResponse.put("totalPrice", 0);
            emptyResponse.put("totalcourses", 0);
            emptyResponse.put("totalElements", 0);
            emptyResponse.put("totalPages", 0);
            emptyResponse.put("numberOfElements", 0);
            emptyResponse.put("size", items.getSize());
            emptyResponse.put("number", items.getNumber());
            return ResponseEntity.ok(emptyResponse);
        }
        Page<Map<String,Object>> wishListItems=items.map(
                s->{
                    Map<String,Object> map=new HashMap<>();
                    map.put("coursename",s.getCourse().getName());
                    map.put("paid",s.getCourse().getPaid());
                    map.put("price",s.getCourse().getPrice());
                    map.put("rate",s.getCourse().getRating());
                    map.put("id",s.getCourse().getId());
                    map.put("duration",s.getCourse().getDuration());
                    map.put("courseteacher",s.getCourse().getTeacher().getUsername());
                    map.put("totalNumberOfStudents",s.getCourse().getNumberOfStudents());
                    map.put("description",s.getCourse().getDescription());
                    map.put("coursepicture",s.getCourse().getImageUrl());
                    return map;
                });
        Double totalPrice = myWishListItemsRepo.getTotalCartPrice(student.getId());
        long totalCourses=items.stream().count();
        Map<String, Object> response = new HashMap<>();
        response.put("items", wishListItems);
        response.put("totalPrice", totalPrice);
        response.put("totalcourses", totalCourses);
        response.put("totalElements", items.getTotalElements());
        response.put("totalPages", items.getTotalPages());
        response.put("numberOfElements", items.getNumberOfElements());
        response.put("size", items.getSize());
        response.put("number", items.getNumber());
        return ResponseEntity.ok(response);
    }
    @Override
    public ResponseEntity<?> addToMyCart(Long courseId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() ||
                auth.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }
        String username = auth.getName();
        StudentEntity student = studentRepo.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        CourseEntity course=this.courseRepo.findById(courseId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        CartEntity cart=this.cartRepo.findByStudent(student);
        if(this.myCartItemRepo.existsByCourseAndStudent(course,student)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Course already exists");
        }
        if(this.myCoursesItemRepo.existsByCourseAndStudent(course,student)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("You already bought this course");
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
    public ResponseEntity<?> addToMyWishList(Long courseId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() ||
                auth.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }
        String username = auth.getName();
        StudentEntity student = studentRepo.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        CourseEntity course=this.courseRepo.findById(courseId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        WishListEntity wishList=this.wishListRepo.findByStudent(student);
        if(this.myWishListItemsRepo.existsByCourseAndStudent(course,student)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Course already exists");
        }
        if(this.myCoursesItemRepo.existsByCourseAndStudent(course,student)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("You already bought this course");
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
    public ResponseEntity<?> addToMyCourses(CheckoutRequestDTO coursed) {
        List<Long> ids=coursed.courses().stream()
                .map(CoursesCheckOutReqDTO::courseId).toList();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() ||
                auth.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }
        String username = auth.getName();
        StudentEntity student = studentRepo.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "student not found"));
        List<CourseEntity> courses=new ArrayList<>();
        for(Long id:ids){
            courses.add(this.courseRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found")));
        }
        StudentMyCourseEntity myCourse=this.myCourseRepo.findByStudent(student);

        for(CourseEntity course:courses){
            if(this.myCoursesItemRepo.existsByCourseAndStudent(course,student)){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Course already exists");
            }
        }
        for(CourseEntity course:courses){
            CourseProgressEntity progress=CourseProgressEntity.builder()
                    .course(course)
                    .student(student)
                    .createdAt(LocalDateTime.now())
                    .currentVideoIndex(0)
                    .build();
            this.courseProgressRepo.save(progress);
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
        }
        return ResponseEntity.ok("Added course to cart");
    }
    @Override
    public ResponseEntity<?> deleteFromMyWishList(Long courseId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() ||
                auth.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }
        String username = auth.getName();
        StudentEntity student = studentRepo.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        CourseEntity course=this.courseRepo.findById(courseId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        WishListEntity wishList=this.wishListRepo.findByStudent(student);
        if(!this.myWishListItemsRepo.existsByCourseAndStudent(course,student)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Course doesnot exist");
        }
        WishlistItemEntity item =this.myWishListItemsRepo.findByCourse(course);
        item.setDeletedAt(LocalDateTime.now());
        wishList.getWishlistItem().remove(item);
        this.wishListRepo.save(wishList);
        return ResponseEntity.ok("Removed course from wishList");
    }
    @Override
    public ResponseEntity<?> deleteFromMyCart(Long courseId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() ||
                auth.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }
        String username = auth.getName();
        StudentEntity student = studentRepo.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        CourseEntity course=this.courseRepo.findById(courseId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        CartEntity cart=this.cartRepo.findByStudent(student);
        if(!this.myCartItemRepo.existsByCourseAndStudent(course,student)){
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
    public ResponseEntity<?> deleteFromMyCourses(Long courseId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() ||
                auth.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }
        String username = auth.getName();
        StudentEntity student = studentRepo.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        CourseEntity course=this.courseRepo.findById(courseId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        StudentMyCourseEntity myCourse=this.myCourseRepo.findByStudent(student);
        this.courseProgressRepo.delete(this.courseProgressRepo.findByCourseAndStudent(course, student));
        if(!this.myCoursesItemRepo.existsByCourseAndStudent(course,student)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Course doesnot exist");
        }
        StudentMyCourseItemEntity item =this.myCoursesItemRepo.findByCourse(course);
        item.setDeletedAt(LocalDateTime.now());
        myCourse.getMyCourseItems().remove(item);
        myCourse.setCoursesCount(myCourse.getCoursesCount()-1);
        this.myCourseRepo.save(myCourse);
        return ResponseEntity.ok("Removed course from myCourses");
    }
    @Override
    public ResponseEntity<?> deleteAllFromMyCart(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() ||
                auth.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }
        String username = auth.getName();
        StudentEntity student = studentRepo.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        CartEntity cart=this.cartRepo.findByStudent(student);
        if(!this.myCartItemRepo.existsByStudent(student)){
            System.out.println("no courses to delete");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("no courses");
        }
        cart.getCartItems().clear();
        cart.setTotalPrice(0.0);
        cartRepo.save(cart);
        System.out.println("Done");
        return ResponseEntity.ok("Removed all courses from cart");
    }
    @Override
    public ResponseEntity<?> deleteAllFromMyWishList(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() ||
                auth.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }
        String username = auth.getName();
        StudentEntity student = studentRepo.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        WishListEntity wishlist=this.wishListRepo.findByStudent(student);
        if(!this.myWishListItemsRepo.existsByStudent(student)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("no courses");
        }
        wishlist.getWishlistItem().clear();
        wishListRepo.save(wishlist);
        return ResponseEntity.ok("Removed course from cart");
    }
}
