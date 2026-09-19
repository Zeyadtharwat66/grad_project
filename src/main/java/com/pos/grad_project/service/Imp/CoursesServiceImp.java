package com.pos.grad_project.service.Imp;
import com.pos.grad_project.model.dto.*;
import com.pos.grad_project.model.entity.*;
import com.pos.grad_project.model.enums.Grade;
import com.pos.grad_project.model.mapper.CourseMapper;
import com.pos.grad_project.model.mapper.NotesMapper;
import com.pos.grad_project.repository.*;
import com.pos.grad_project.service.CoursesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class CoursesServiceImp implements CoursesService {
    private final CourseRepo courseRepo;
    private final TeacherRepo teacherRepo;
    private final CategoryRepo categoryRepo;
    private final CourseMapper courseMapper;
    private final NotesRepo notesRepo;
    private final SectionRepo sectionRepo;
    private final StudentRepo studentRepo;
    private final CourseFeedbackRepo feedbackRepo;
    private final CourseProgressRepo courseProgressRepo;
    private final VideosRepo videosRepo;
    private final StudentVideoProgressRepo studentVideoProgressRepo;
    private final MyCoursesItemRepo myCoursesItemRepo;

    @Override
    public ResponseEntity<?> allCourses(CoursesReqDTO coursesReqDTO,int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<CourseEntity> courseEntity = this.courseRepo.findByCategoryAndGrade(categoryRepo.findByName(coursesReqDTO.category()),coursesReqDTO.grade(),pageable);
        //return ResponseEntity.status(HttpStatus.OK).body(courseEntity);
        //or
        List<CourseResDTO> data=this.courseMapper.toRespDTO(courseEntity.getContent());
        Map<String, Object> response = new HashMap<>();
        response.put("content", data);
        response.put("page", courseEntity.getNumber());
        response.put("size", courseEntity.getSize());
        response.put("totalElements", courseEntity.getTotalElements());
        response.put("totalPages", courseEntity.getTotalPages());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @Override
    public ResponseEntity<?> courseContent(Long id) {
        CourseEntity courseEntity = this.courseRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "course not found"));
        TeacherEntity teacher=courseEntity.getTeacher();
        List<Map<String, Object>> sectionInfoList = courseEntity.getSections()
                .stream()
                .map(section -> {
                    Map<String, Object> sectionMap = new HashMap<>();
                    List<Map<String, Object>> videoList = section.getVideo()
                            .stream()
                            .map(video -> {
                                Map<String, Object> videoMap = new HashMap<>();
                                videoMap.put("title", video.getTitle());
                                videoMap.put("duration", video.getDuration());
                                videoMap.put("id", video.getId());
                                return videoMap;
                            })
                            .collect(Collectors.toList());
                    sectionMap.put("videos", videoList);
                    sectionMap.put("title", section.getTitle());
                    sectionMap.put("duration", section.getDuration());
                    sectionMap.put("numberOfLessons", section.getTotalLessons());
                    return sectionMap;
                })
                .collect(Collectors.toList());
        Map<String, Object> response = new HashMap<>();
        response.put("paid",courseEntity.getPaid());
        response.put("price",courseEntity.getPrice());
        response.put("grade", courseEntity.getGrade());
        response.put("track",courseEntity.getCategory().getName());
        response.put("teacherName",teacher.getUsername());
        response.put("teacherImage",teacher.getProfilePictureUrl());
        response.put("teacherRate",teacher.getRate());
        response.put("teacherNumberOfStudents",teacher.getNumberOfStudents());
        response.put("numberOfCourses",teacher.getNumberOfCourses());
        response.put("teacherBio",teacher.getBio());
        response.put("sections",sectionInfoList);
        response.put("numberOfVideos",courseEntity.getVideos().size());
        response.put("numberOfSections",courseEntity.getSections().size());
        response.put("totalLength",courseEntity.getDuration());
        response.put("name",courseEntity.getName());
        response.put("description",courseEntity.getDescription());
        response.put("numberOfComments",courseEntity.getFeedbacks().size());
        response.put("numberOfStudents",courseEntity.getNumberOfStudents());
        response.put("createdAt",courseEntity.getCreatedAt());
        response.put("courseRate",courseEntity.getRating());
        response.put("courseImage",courseEntity.getImageUrl());
        return ResponseEntity.ok(response);
    }
    @Override
    public ResponseEntity<?> courseInfo(Long id) {
        CourseEntity courseEntity = this.courseRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "course not found"));
        String name = courseEntity.getName();
        String description = courseEntity.getDescription();
        int numberOfComments=courseEntity.getFeedbacks().size();
        String teacherName=courseEntity.getTeacher().getUsername();
        int numberOfStudents=courseEntity.getNumberOfStudents();
        LocalDateTime createdAt=courseEntity.getCreatedAt();
        float rate=courseEntity.getRating();
        Map<String, Object> response = new HashMap<>();
        response.put("name",name);
        response.put("description",description);
        response.put("numberOfComments",numberOfComments);
        response.put("teacherName",teacherName);
        response.put("numberOfStudents",numberOfStudents);
        response.put("createdAt",createdAt);
        response.put("rate",rate);
        return ResponseEntity.ok(response);
    }
    @Override
    public ResponseEntity<?> teacherOfCourseInfo(Long id) {
        CourseEntity courseEntity = this.courseRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "course not found"));
        TeacherEntity teacher=courseEntity.getTeacher();
        String teacherUsername=teacher.getUsername();
        double teacherRate=teacher.getRate();
        int numberOfStudents=teacher.getNumberOfStudents();
        int numberOfCourses=teacher.getNumberOfCourses();
        String bio=teacher.getBio();
        Map<String, Object> response = new HashMap<>();
        response.put("teacher name",teacherUsername);
        response.put("rate",teacherRate);
        response.put("numberOfStudents",numberOfStudents);
        response.put("numberOfCourses",numberOfCourses);
        response.put("bio",bio);
        return ResponseEntity.ok(response);
    }
    @Override
    public ResponseEntity<?> getCourseReviews(Long id) {
        CourseEntity courseEntity = this.courseRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "course not found"));
        List<Map<String,Object>> feedbacks=courseEntity.getFeedbacks()
                .stream().map(f->{
                    Map<String,Object> map=new HashMap<>();
                    map.put("comment",f.getComment());
                    map.put("rate",f.getRating());
                    map.put("username",f.getStudent().getUsername());
                    return map;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(feedbacks);
    }
    @Override
    public ResponseEntity<?> addCourseReviews(Long id, String comment, float rate) {
        CourseEntity courseEntity = this.courseRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        StudentEntity student = getAuthenticatedStudent();

        if (!myCoursesItemRepo.existsByCourseAndStudent(courseEntity, student)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You must enroll in the course before reviewing it");
        }
        if (rate < 0 || rate > 5) {
            return ResponseEntity.badRequest().body("Rating must be between 0 and 5");
        }
        if (comment == null || comment.isBlank()) {
            return ResponseEntity.badRequest().body("Comment is required");
        }

        CourseFeedbackEntity courseFeedbackEntity = CourseFeedbackEntity.builder()
                .course(courseEntity)
                .comment(comment.trim())
                .rating(rate)
                .student(student)
                .build();
        this.feedbackRepo.save(courseFeedbackEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(courseFeedbackEntity.getId());
    }
    @Override
    public ResponseEntity<?> relatedCourses(Long id) {
        CourseEntity courseEntity = this.courseRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "course not found"));
        List<CourseEntity> relatedCourses = this.courseRepo.findTop3ByCategoryAndGradeAndIdNot(courseEntity.getCategory(), courseEntity.getGrade(),id);
        List<Map<String,Object>> response=relatedCourses.stream().map(
                s->{
                    Map<String,Object> map=new HashMap<>();
                    map.put("coursename",s.getName());
                    map.put("price",s.getPrice());
                    map.put("rate",s.getRating());
                    map.put("duration",s.getDuration().toHours());
                    map.put("totalNumberOfStudents",s.getNumberOfStudents());
                    map.put("description",s.getDescription());
                    map.put("id",s.getId());
                    map.put("courseteacher",s.getTeacher().getUsername());
                    map.put("coursepicture",s.getImageUrl());
                    return map;
                }).toList();
        return ResponseEntity.ok(response);
    }
    @Override
    public ResponseEntity<?> findCoursesByFilters(String grade,Double minPrice,Double maxPrice,Float rate,String name,Long teacher,String category,int page, int size) {
        Specification<CourseEntity> spec = Specification.where(null);
        Grade gradeEnum = null;
        if (grade != null && !grade.isEmpty()) {
            try {
                gradeEnum = Grade.valueOf(grade.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Invalid grade value");
            }
        }
        final Grade finalGradeEnum = gradeEnum;
        if (gradeEnum != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("grade"), finalGradeEnum));
        }
        if (name != null) {
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("name"), "%" + name + "%"));
        }
        if (minPrice != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }
        if (maxPrice != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }
        if (rate != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("rating"), rate));
        }
        if (teacher != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("teacher").get("id"), teacher));
        }
        if (category != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("category").get("name"), category));
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<CourseEntity> result = courseRepo.findAll(spec, pageable);
        List<HashMap<String,Object>> response=result.stream()
                .map(s->{
                    HashMap<String,Object> x = new HashMap<>();
                    x.put("id",s.getId());
                    x.put("name",s.getName());
                    x.put("description",s.getDescription());
                    x.put("rating",s.getRating());
                    x.put("teacher",s.getTeacher().getUsername());
                    x.put("category",s.getCategory().getName());
                    x.put("duration",s.getDuration());
                    x.put("grade",s.getGrade());
                    x.put("imageUrl",s.getImageUrl());
                    x.put("numberOfStudents",s.getNumberOfStudents());
                    x.put("paid",s.getPaid());
                    x.put("price",s.getPrice());
                    return x;
                }).collect(Collectors.toList());
        HashMap<String, Object> responseBody = new HashMap<>();
        responseBody.put("courses", response);
        responseBody.put("totalElements", result.getTotalElements());
        responseBody.put("totalPages", result.getTotalPages());
        responseBody.put("numberOfElements", result.getNumberOfElements());
        responseBody.put("size", result.getSize());
        responseBody.put("number", result.getNumber());
        return ResponseEntity.ok(responseBody);
    }
    @Override
    public ResponseEntity<?> searchCourse(String name,int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<CourseEntity> result=this.courseRepo.findAllByName(name,pageable);
        return ResponseEntity.ok(result);
    }
    @Override
    public ResponseEntity<?> showMaterial(long sectionId) {
        SectionEntity section = this.sectionRepo.findById(sectionId);
        if (section == null || section.getCourse() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Section not found");
        }

        StudentEntity student = getAuthenticatedStudent();
        if (!myCoursesItemRepo.existsByCourseAndStudent(section.getCourse(), student)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not enrolled in this course");
        }
        if (section.getMaterial() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Material not found");
        }

        HashMap<String, Object> response = new HashMap<>();
        response.put("material", section.getMaterial().getFileUrl());
        response.put("type", section.getMaterial().getType());
        response.put("title", section.getMaterial().getTitle());
        return ResponseEntity.ok(response);
    }
    @Override
    public ResponseEntity<?> showVideosPage(long courseId) {
        StudentEntity student = getAuthenticatedStudent();
        long studentId = student.getId();

        CourseEntity course = this.courseRepo.findById(courseId);
        if (course == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }
        if (!myCoursesItemRepo.existsByCourseAndStudent(course, student)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not enrolled in this course");
        }

        List<Map<String, Object>> sectionInfoList = course.getSections()
                .stream()
                .map(section -> {
                    Map<String, Object> sectionMap = new HashMap<>();
                    List<Map<String, Object>> videoList = section.getVideo()
                            .stream()
                            .map(video -> {
                                Map<String, Object> videoMap = new HashMap<>();
                                videoMap.put("title", video.getTitle());
                                videoMap.put("duration", GeneralService.formatDuration(video.getDuration()));
                                videoMap.put("id", video.getId());
                                videoMap.put("url",video.getUrl());
                                videoMap.put("isCompleted",Optional.ofNullable(this.studentVideoProgressRepo.findByVideoIdAndStudentId(video.getId(), studentId)).map(StudentVideoProgressEntity::isCompleted).orElse(false));
                                videoMap.put("description",video.getDescription());
                                return videoMap;
                            })
                            .collect(Collectors.toList());
                    sectionMap.put("id", section.getId());
                    sectionMap.put("videos", videoList);
                    sectionMap.put("title", section.getTitle());
                    sectionMap.put("duration",  GeneralService.formatDuration(section.getDuration()));
                    sectionMap.put("numberOfLessons", section.getTotalLessons());
                    sectionMap.put("resources",section.getMaterial() != null ? section.getMaterial().getFileUrl() : null);
                    sectionMap.put("resourcesId",section.getMaterial() != null ? section.getMaterial().getId() : null);
                    sectionMap.put("materialtype",section.getMaterial() != null ? section.getMaterial().getType() : null);
                    sectionMap.put("materialtitle",section.getMaterial() != null ? section.getMaterial().getTitle() : null);
                    return sectionMap;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(sectionInfoList);
    }
    @Override
    public ResponseEntity<?> addNote(NoteReqDTO noteReqDTO) {
        StudentEntity student = getAuthenticatedStudent();
        VideoEntity video = this.videosRepo.findById(noteReqDTO.lessonId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found"));

        if (!myCoursesItemRepo.existsByCourseAndStudent(video.getCourse(), student)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not enrolled in this course");
        }
        if (notesRepo.existsByNoteAndStudentIdAndVideosId(
                noteReqDTO.text(), student.getId(), video.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Note already exists");
        }

        NotesEntity newNote = NotesEntity.builder()
                .note(noteReqDTO.text().trim())
                .student(student)
                .videos(video)
                .time(noteReqDTO.time())
                .build();
        student.getNotes().add(newNote);
        studentRepo.save(student);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "note", newNote.getNote(),
                "time", newNote.getTime(),
                "id", newNote.getId()
        ));
    }
    @Override
    public ResponseEntity<?> updateNote(UpdateNoteRequest request, long noteId) {
        StudentEntity student = getAuthenticatedStudent();
        NotesEntity theNote = notesRepo.findById(noteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));

        if (!theNote.getStudent().getId().equals(student.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not own this note");
        }
        if (notesRepo.existsByNoteAndStudentIdAndVideosId(
                request.text(), student.getId(), theNote.getVideos().getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Note already exists");
        }

        theNote.setNote(request.text().trim());
        notesRepo.save(theNote);
        return ResponseEntity.ok(theNote.getId());
    }

    @Override
    public ResponseEntity<?> deleteNote(long noteId) {
        StudentEntity student = getAuthenticatedStudent();
        NotesEntity theNote = notesRepo.findById(noteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));

        if (!theNote.getStudent().getId().equals(student.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not own this note");
        }

        theNote.setDeletedAt(LocalDateTime.now());
        notesRepo.save(theNote);
        return ResponseEntity.ok("Note deleted successfully");
    }
    @Override
    public ResponseEntity<?> getNote(long videoId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() ||
                auth.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }
        String username = auth.getName();
        StudentEntity student=this.studentRepo.findByUsername(username).orElseThrow(()->new RuntimeException("Student not found"));
        List<NotesEntity> notes=this.notesRepo.findByVideosIdAndStudentId(videoId,student.getId());
        List<HashMap<String, Object>> responseBody = notes.stream().map(
                s->{
                    HashMap<String, Object> responseBodyMap = new HashMap<>();
                    responseBodyMap.put("note",s.getNote());
                    responseBodyMap.put("id",s.getId());
                    responseBodyMap.put("time",s.getTime());
                    return responseBodyMap;
                }
        ).toList();
        return ResponseEntity.ok(responseBody);
    }
    @Override
    public ResponseEntity<?> getProgress(long courseId) {
        StudentEntity student = getAuthenticatedStudent();
        CourseEntity course = courseRepo.findById(courseId);
        if (course == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }

        CourseProgressEntity progress = courseProgressRepo.findByCourseAndStudent(course, student);
        if (progress == null || course.getTotalLessons() == 0) {
            return ResponseEntity.ok(0.0);
        }

        double myProgress = ((double) progress.getCurrentVideoIndex() / course.getTotalLessons()) * 100.0;
        return ResponseEntity.ok(Math.min(myProgress, 100.0));
    }
    @Override
    public ResponseEntity<?> completeVideo(long id) {
        StudentEntity student = getAuthenticatedStudent();
        VideoEntity video = videosRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found"));

        if (!myCoursesItemRepo.existsByCourseAndStudent(video.getCourse(), student)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not enrolled in this course");
        }

        StudentVideoProgressEntity progress =
                studentVideoProgressRepo.findByVideoIdAndStudentId(video.getId(), student.getId());

        if (progress == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Video progress not found");
        }

        progress.setCompleted(true);
        studentVideoProgressRepo.save(progress);

        int allVideos = studentVideoProgressRepo.countByStudentIdAndCourseId(
                student.getId(), video.getCourse().getId());
        int completedVideos = studentVideoProgressRepo.countByStudentIdAndCompletedAndCourseId(
                student.getId(), true, video.getCourse().getId());

        StudentMyCourseItemEntity item =
                myCoursesItemRepo.findByCourseIdAndStudentId(video.getCourse().getId(), student.getId());

        if (item != null && allVideos > 0) {
            item.setProgress(((double) completedVideos / allVideos) * 100.0);
            item.setIsCompleted(completedVideos == allVideos);
            myCoursesItemRepo.save(item);
        }

        return ResponseEntity.ok("Video completed");
    }
    private StudentEntity getAuthenticatedStudent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not logged in");
        }
        return studentRepo.findByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Student not found"));
    }
}

