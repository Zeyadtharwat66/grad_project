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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
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
    private final VideoCommentsRepo videoCommentRepo;
    private final VideosRepo videosRepo;
    private final StudentVideoProgressRepo studentVideoProgressRepo;
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
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }
        StudentEntity studentEntity = StudentEntity.builder()
                .username(registerDTO.username())
                .password(passwordEncoder.encode(registerDTO.password()))
                .role("STUDENT")
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
        StudentEntity authenticatedStudent = getAuthenticatedStudent();
        if (!authenticatedStudent.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only access your own profile");
        }
        StudentRespDTO res = this.studentMapper.toRespDTO(authenticatedStudent);
        return ResponseEntity.ok(res);
    }
    @Override
    public ResponseEntity<?> changePassword(ChangePasswordRequest request) {
        StudentEntity student = getAuthenticatedStudent();
        if (!passwordEncoder.matches(request.oldPassword(), student.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password does not match");
        }
        student.setPassword(passwordEncoder.encode(request.newPassword()));
        this.studentRepo.save(student);
        return ResponseEntity.ok("Password changed");
    }

    @Override
    public ResponseEntity<?> changeUsername(ChangeUsernameRequest request) {
        StudentEntity student = getAuthenticatedStudent();
        if (student.getUsername().equals(request.newUsername())) {
            return ResponseEntity.badRequest().body("New username must be different");
        }
        if (this.studentRepo.findByUsername(request.newUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }
        student.setUsername(request.newUsername());
        this.studentRepo.save(student);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                student.getUsername(),
                null,
                List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority(student.getRole()))
        );
        String token = jwtTokenService.generateJWTToken(authentication);

        return ResponseEntity.ok(Map.of(
                "message", "Username changed",
                "token", token,
                "username", student.getUsername()
        ));
    }

    @Override
    public ResponseEntity<?> changeProfilePicture(ChangeProfilePictureRequest request) {
        StudentEntity student = getAuthenticatedStudent();
        student.setProfilePictureUrl(request.profilePicture());
        this.studentRepo.save(student);
        return ResponseEntity.ok("Profile picture changed");
    }

    private StudentEntity getAuthenticatedStudent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not logged in");
        }
        return studentRepo.findByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Student not found"));
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
                    map.put("isCompleted",s.getIsCompleted());
                    map.put("totalNumberOfStudents",s.getCourse().getNumberOfStudents());
                    map.put("description",s.getCourse().getDescription());
                    map.put("coursepicture",s.getCourse().getImageUrl());
                    map.put("grade",s.getCourse().getGrade());
                    map.put("track",s.getCourse().getCategory().getName());
                    map.put("teacherName",s.getCourse().getTeacher().getUsername());
                    map.put("numberOfVideos",s.getCourse().getTotalLessons());
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
    @Transactional
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
        StudentMyCourseEntity myCourse = this.myCourseRepo.findByStudent(student);
        if (myCourse == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Student course collection not initialized");
        }

        for (CourseEntity course : courses) {
            if (this.myCoursesItemRepo.existsByCourseAndStudent(course, student)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Course already exists");
            }
        }

        for (CourseEntity course : courses) {
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
            for(VideoEntity video:course.getVideos()){
                StudentVideoProgressEntity prog=StudentVideoProgressEntity.builder()
                        .student(student)
                        .completed(false)
                        .video(video)
                        .course(course)
                        .createdAt(LocalDateTime.now())
                        .build();
                this.studentVideoProgressRepo.save(prog);
            }
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
        WishlistItemEntity item = this.myWishListItemsRepo.findByCourseAndStudent(course, student);
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
        CartItemEntity item = this.myCartItemRepo.findByCourseAndStudent(course, student);
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
        StudentMyCourseEntity myCourse = this.myCourseRepo.findByStudent(student);
        if (!this.myCoursesItemRepo.existsByCourseAndStudent(course, student)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Course does not exist");
        }

        CourseProgressEntity progress = this.courseProgressRepo.findByCourseAndStudent(course, student);
        if (progress != null) {
            this.courseProgressRepo.delete(progress);
        }

        StudentMyCourseItemEntity item = this.myCoursesItemRepo.findByCourseIdAndStudentId(course.getId(), student.getId());
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
    @Override
    public ResponseEntity<?> addComment(CommentReqDTO commentReqDTO) {
        String comment=commentReqDTO.comment();
        float rate=commentReqDTO.rate();
        System.out.println(comment);
        System.out.println(commentReqDTO.lessonId());
        if(this.videoCommentRepo.existsByComment(comment)){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        long videoId=commentReqDTO.lessonId();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() ||
                auth.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }
        String username = auth.getName();
        StudentEntity student=this.studentRepo.findByUsername(username).orElseThrow(()->new RuntimeException("Student not found"));
        VideoEntity video=this.videosRepo.findById(videoId).orElseThrow(()->new RuntimeException("Video not found"));
        VideoCommentEntity newComment=VideoCommentEntity.builder()
                .comment(comment)
                .student(student)
                .video(video)
                .createdAt(LocalDateTime.now())
                .rating(rate)
                .build();
        student.getVideoComment().add(newComment);
        this.studentRepo.save(student);
        HashMap<String ,Object> response = new HashMap<>();
        response.put("text",newComment.getComment());
        response.put("videoId",videoId);
        response.put("username",username);
        response.put("time",newComment.getCreatedAt());
        response.put("image",student.getProfilePictureUrl());
        response.put("commentId",newComment.getId());
        return ResponseEntity.ok(response);
    }
    @Override
    public ResponseEntity<?> getComments(Long videoId) {
        List<VideoCommentEntity> comments=this.videoCommentRepo.findByVideoId(videoId);
        List<HashMap<String, Object>> responseBody = comments.stream().map(
                s->{
                    HashMap<String, Object> responseBodyMap = new HashMap<>();
                    responseBodyMap.put("username",s.getStudent().getUsername());
                    responseBodyMap.put("image",s.getStudent().getProfilePictureUrl());
                    responseBodyMap.put("time",s.getCreatedAt());
                    responseBodyMap.put("text",s.getComment());
                    responseBodyMap.put("id",s.getId());
                    responseBodyMap.put("rate",s.getRating());
                    return responseBodyMap;
                }
        ).toList();
        return ResponseEntity.ok(responseBody);
    }
}
