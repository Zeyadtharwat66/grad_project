package com.pos.grad_project.service.Imp;

import com.pos.grad_project.model.dto.CoursePageResDTO;
import com.pos.grad_project.model.dto.CourseResDTO;
import com.pos.grad_project.model.dto.CoursesReqDTO;
import com.pos.grad_project.model.entity.*;
import com.pos.grad_project.model.enums.Grade;
import com.pos.grad_project.model.mapper.CourseMapper;
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
    private final CourseProgressRepo courseProgressRepo;
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
        int numberOfVideos=courseEntity.getVideos().size();
        int numberOfSections=courseEntity.getSections().size();
        Duration duration=courseEntity.getDuration();
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
        response.put("sections",sectionInfoList);
        response.put("numberOfVideos",numberOfVideos);
        response.put("numberOfSections",numberOfSections);
        response.put("totalLength",duration);
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
        String name=teacher.getUsername();
        double rate=teacher.getRate();
        int numberOfStudents=teacher.getNumberOfStudents();
        int numberOfCourses=teacher.getNumberOfCourses();
        String bio=teacher.getBio();
        Map<String, Object> response = new HashMap<>();
        response.put("name",name);
        response.put("rate",rate);
        response.put("numberOfStudents",numberOfStudents);
        response.put("numberOfCourses",numberOfCourses);
        response.put("bio",bio);
        return ResponseEntity.ok(response);
    }
    @Override
    public ResponseEntity<?> courseReview(Long id) {
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
    public ResponseEntity<?> relatedCourses(Long id) {
        CourseEntity courseEntity = this.courseRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "course not found"));
        Grade grade=courseEntity.getGrade();
        CategoryEntity category=courseEntity.getCategory();
        List<CourseEntity> relatedCourses = this.courseRepo
                .findByCategoryAndGrade(category, grade)
                .stream()
                .filter(c -> !c.getId().equals(courseEntity.getId()))
                .limit(3)
                .collect(Collectors.toList());
        return ResponseEntity.ok(relatedCourses);
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
        SectionEntity section=this.sectionRepo.findById(sectionId);
        HashMap<String, Object> response = new HashMap<>();
        response.put("material",section.getMaterial().getFileUrl());
        response.put("type",section.getMaterial().getType());
        response.put("title",section.getMaterial().getTitle());
        return ResponseEntity.ok(response);
    }
    @Override
    public ResponseEntity<?> addNote(String note, long sectionId, long studentId) {
        StudentEntity student=this.studentRepo.findById(studentId);
        SectionEntity section=this.sectionRepo.findById(sectionId);
        if(this.notesRepo.existsByNote(note)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Note already exists");
        }
        NotesEntity newNote=NotesEntity.builder()
                .note(note)
                .student(student)
                .section(section)
                .build();
        student.getNotes().add(newNote);
        this.studentRepo.save(student);
        return ResponseEntity.ok(newNote.getId());
    }
    @Override
    public ResponseEntity<?> updateNote(String note, long noteId,long studentId) {
        StudentEntity student=this.studentRepo.findById(studentId);
        if(this.notesRepo.existsByNote(note)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("No Change Occurred");
        }
        NotesEntity theNote=this.notesRepo.findById(noteId);
        student.getNotes().remove(theNote);
        theNote.setNote(note);
        student.getNotes().add(theNote);
        this.studentRepo.save(student);
        return ResponseEntity.ok(theNote.getId());
    }
    @Override
    public ResponseEntity<?> deleteNote(long noteId,long studentId) {
        StudentEntity student=this.studentRepo.findById(studentId);
        NotesEntity theNote=this.notesRepo.findById(noteId);
        student.getNotes().remove(theNote);
        this.studentRepo.save(student);
        return ResponseEntity.ok("Note Deleted Successfully");
    }
    @Override
    public ResponseEntity<?> getProgress(long studentId, long courseId) {//lesa
        StudentEntity student=this.studentRepo.findById(studentId);
        CourseEntity course=this.courseRepo.findById(courseId);
        CourseProgressEntity progress=this.courseProgressRepo.findByCourseAndStudent(course, student);
        double myProgress=(progress.getCurrentVideoIndex()/course.getTotalLessons())*100;
        return ResponseEntity.ok(myProgress);
    }

}
