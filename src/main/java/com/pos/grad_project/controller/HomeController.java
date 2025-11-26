package com.pos.grad_project.controller;

import com.pos.grad_project.service.HomeService;
import com.pos.grad_project.service.TeacherService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/home")
public class HomeController {
    private final HomeService homeService;
    @GetMapping("top2-teachers")
    public ResponseEntity<?> top2(){
        return this.homeService.top2Teachers();
    }
    @GetMapping("top6courses")
    public ResponseEntity<?> top6Courses() {
        return this.homeService.top6Courses();
    }
    @GetMapping("/random4")
    public ResponseEntity<?> random4(){
        return this.homeService.show4Feedback();
    }
    @GetMapping("/statistics")
    public ResponseEntity<?> statistics(){
        return this.homeService.statistics();
    }
}
