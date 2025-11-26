package com.pos.grad_project.model.mapper;

import com.pos.grad_project.model.dto.CourseFeedbackResDTO;
import com.pos.grad_project.model.dto.CoursePageResDTO;
import com.pos.grad_project.model.dto.CourseResDTO;
import com.pos.grad_project.model.entity.CourseEntity;
import com.pos.grad_project.model.entity.CourseFeedbackEntity;
import com.pos.grad_project.model.entity.WishlistItemEntity;
import com.pos.grad_project.repository.MyWishListItemsRepo;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Mapper(componentModel="spring")
public interface CourseMapper {
    public CoursePageResDTO toRespDTO(CourseEntity course);
    public List<CourseResDTO> toRespDTO(List<CourseEntity> courses);
}
