package com.pos.grad_project.service.Imp;

import com.pos.grad_project.model.dto.CourseFeedbackReqDTO;
import com.pos.grad_project.model.dto.CourseFeedbackResDTO;
import com.pos.grad_project.model.entity.CourseEntity;
import com.pos.grad_project.model.entity.CourseFeedbackEntity;
import com.pos.grad_project.model.entity.TeacherEntity;
import com.pos.grad_project.model.mapper.CourseFeedbackMapper;
import com.pos.grad_project.repository.CourseFeedbackRepo;
import com.pos.grad_project.repository.CourseRepo;
import com.pos.grad_project.repository.StudentRepo;
import com.pos.grad_project.repository.TeacherRepo;
import com.pos.grad_project.service.HomeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Service
@AllArgsConstructor
public class HomeServiceImp implements HomeService {
    private final TeacherRepo teacherRepo;
    private final CourseRepo courseRepo;
    private final CourseFeedbackRepo courseFeedbackRepo;
    private final StudentRepo studentRepo;
    private final CourseFeedbackMapper courseFeedbackMapper;

    @Override
    public ResponseEntity<?> top2Teachers() {
        List<TeacherEntity> list=this.teacherRepo.findTop2ByOrderByNumberOfStudentsDesc();
        List<HashMap<String, Object>> response = list.stream().map(s-> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", s.getId());
            map.put("name",s.getUsername());
            map.put("picture",s.getProfilePictureUrl());
            map.put("rate",s.getRate());
            map.put("subject",s.getSpecialization());
            map.put("numberOfStudents",s.getNumberOfStudents());
            map.put("numberOfCourses",s.getNumberOfCourses());
            map.put("bio",s.getBio());
            return map;
        }).toList();
        return ResponseEntity.ok(response);
    }
    @Override
    public ResponseEntity<?> top6Courses() {
        List<CourseEntity> courseEntity = this.courseRepo.findTop6ByOrderByNumberOfStudentsDesc();
        List<HashMap<String, Object>> response = courseEntity.stream().map(s->{
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", s.getId());
            map.put("name", s.getName());
            map.put("duration", s.getDuration());
            map.put("teacher", s.getTeacher().getUsername());
            map.put("paid", s.getPaid());
            map.put("price",s.getPrice());
            map.put("rating",s.getRating());
            map.put("grade",s.getGrade());
            map.put("numberOfStudents",s.getNumberOfStudents());
            map.put("image",s.getImageUrl());
            map.put("description",s.getDescription());
            map.put("totalLessons",s.getTotalLessons());
            return map;
        }).toList();

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> statistics() {
        HashMap<String,Long> map = new HashMap<>();
        map.put("Students",this.studentRepo.countByDeletedAtIsNull());
        map.put("Teachers",this.teacherRepo.countByDeletedAtIsNull());
        map.put("Courses",this.courseRepo.countByDeletedAtIsNull());
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<?> getImages() {
        List<String> imgs=this.teacherRepo.findAllProfile_picture_url();
        return ResponseEntity.ok(imgs);
    }

    @Override
    public ResponseEntity<?> show6Feedback() {
        List<CourseFeedbackEntity> feedback=this.courseFeedbackRepo.findRandom6();
        List<HashMap<String,Object>> feedback2=feedback.stream().map(s->{
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", s.getId());
            map.put("comment", s.getComment());
            map.put("rating", s.getRating());
            map.put("student", s.getStudent().getUsername());
            map.put("course", s.getCourse().getName());
            map.put("studentImage",s.getStudent().getProfilePictureUrl());
            return map;
        }).toList();
        return new ResponseEntity<>(feedback2, HttpStatus.OK);
    }

}
