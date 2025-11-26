package com.pos.grad_project.service.Imp;

import com.pos.grad_project.model.dto.CourseFeedbackReqDTO;
import com.pos.grad_project.model.dto.CourseFeedbackResDTO;
import com.pos.grad_project.model.dto.CourseFeedbackUpdateReqDTO;
import com.pos.grad_project.model.entity.CourseFeedbackEntity;
import com.pos.grad_project.model.mapper.CourseFeedbackMapper;
import com.pos.grad_project.repository.CourseFeedbackRepo;
import com.pos.grad_project.service.CourseFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
@Service
public class CourseFeedbackServiceImp implements CourseFeedbackService {
    private final CourseFeedbackRepo courseFeedbackRepo;
    private final CourseFeedbackMapper courseFeedbackMapper;
    @Override
    public ResponseEntity<?> addFeedback(CourseFeedbackReqDTO courseFeedbackReqDTO) {
        CourseFeedbackEntity courseFeedbackEntity = this.courseFeedbackMapper.toEntity(courseFeedbackReqDTO);
        courseFeedbackRepo.save(courseFeedbackEntity);
        return new ResponseEntity<>(courseFeedbackEntity, HttpStatus.CREATED);
    }



    @Override
    public ResponseEntity<?> updateFeedback(CourseFeedbackUpdateReqDTO courseFeedbackupdateReqDTO) {
        CourseFeedbackEntity courseFeedbackEntity = this.courseFeedbackMapper.toEntity(courseFeedbackupdateReqDTO);
        CourseFeedbackEntity ExistingcourseFeedbackEntity=this.courseFeedbackRepo.findById(courseFeedbackupdateReqDTO.feedbackID()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "feedback not found"));
        ExistingcourseFeedbackEntity.setUpdatedAt(LocalDateTime.now());
        ExistingcourseFeedbackEntity.setComment(courseFeedbackEntity.getComment());
        ExistingcourseFeedbackEntity.setRating(courseFeedbackEntity.getRating());
        courseFeedbackRepo.save(ExistingcourseFeedbackEntity);
        return new ResponseEntity<>(ExistingcourseFeedbackEntity, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteFeedback(Long id) {
        CourseFeedbackEntity feedback = this.courseFeedbackRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "feedback not found"));
        feedback.setDeletedAt(LocalDateTime.now());
        courseFeedbackRepo.save(feedback);
        return new ResponseEntity<>(feedback, HttpStatus.OK);
    }
}
