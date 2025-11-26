package com.pos.grad_project.service.Imp;

import com.pos.grad_project.model.dto.CourseFeedbackReqDTO;
import com.pos.grad_project.model.dto.CourseFeedbackResDTO;
import com.pos.grad_project.model.entity.CourseEntity;
import com.pos.grad_project.model.entity.CourseFeedbackEntity;
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
        return ResponseEntity.ok(this.teacherRepo.findTop2ByOrderByNumberOfStudentsDesc());
    }
    @Override
    public ResponseEntity<?> top6Courses() {
        List<CourseEntity> courseEntity = this.courseRepo.findTop6ByOrderByNumberOfStudentsDesc();
        return ResponseEntity.ok(courseEntity);
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
    public ResponseEntity<?> show4Feedback() {
        List<CourseFeedbackEntity> feedback=this.courseFeedbackRepo.findRandom4ByCourseId();
        List<CourseFeedbackResDTO> feedback2=new ArrayList<>();
        for(int i=0;i<feedback.size();i++){
            feedback2.add(this.courseFeedbackMapper.toRespDTO(feedback.get(i)));
        }
        return new ResponseEntity<>(feedback2, HttpStatus.OK);
    }
}
