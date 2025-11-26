package com.pos.grad_project.service.Imp;

import com.pos.grad_project.model.dto.CoursePageResDTO;
import com.pos.grad_project.model.dto.CourseResDTO;
import com.pos.grad_project.model.dto.CoursesReqDTO;
import com.pos.grad_project.model.entity.CategoryEntity;
import com.pos.grad_project.model.entity.CourseEntity;
import com.pos.grad_project.model.entity.SectionEntity;
import com.pos.grad_project.model.entity.TeacherEntity;
import com.pos.grad_project.model.enums.Grade;
import com.pos.grad_project.model.mapper.CourseMapper;
import com.pos.grad_project.repository.CategoryRepo;
import com.pos.grad_project.repository.CourseRepo;
import com.pos.grad_project.repository.TeacherRepo;
import com.pos.grad_project.service.CoursesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<?> findCoursesByFilters(Grade grade,double minPrice,double maxPrice,float rate,String name,String teacher,String category,int page, int size) {
        TeacherEntity teacherEntity = this.teacherRepo.findByUsername(teacher);
        CategoryEntity categoryEntity = this.categoryRepo.findByName(category);
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<CourseEntity> courses = this.courseRepo
                .findByPriceBetweenAndRatingGreaterThanEqualAndCategoryAndGradeAndTeacherAndName(
                        minPrice,maxPrice, rate, categoryEntity, grade, teacherEntity, name, pageable
                );
        return ResponseEntity.ok(courses);
    }

    @Override
    public ResponseEntity<?> searchCourse(String name,int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<CourseEntity> result=this.courseRepo.findAllByName(name,pageable);
        return ResponseEntity.ok(result);
    }
}
