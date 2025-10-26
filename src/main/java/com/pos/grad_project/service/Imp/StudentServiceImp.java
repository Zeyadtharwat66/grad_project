package com.pos.grad_project.service.Imp;

import com.pos.grad_project.model.dto.LoginDTO;
import com.pos.grad_project.model.dto.RegisterDTO;
import com.pos.grad_project.model.entity.StudentEntity;
import com.pos.grad_project.repository.StudentRepo;
import com.pos.grad_project.service.JWTTokenService;
import com.pos.grad_project.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StudentServiceImp implements StudentService {
    private final JWTTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;
    private final StudentRepo studentRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password())
        );
        return jwtTokenService.generateJWTToken(authentication);
    }

    @Override
    public String register(RegisterDTO registerDTO) {//kamel elbaynat lma t23od m3 hani
        if(studentRepo.findByUsername(registerDTO.username()).isPresent()){
            throw new RuntimeException("Username already exists!");
        }
        StudentEntity studentEntity = StudentEntity.builder()
                .username(registerDTO.username())
                .password(passwordEncoder.encode(registerDTO.password()))
                .role(registerDTO.role())
                .build();
        studentRepo.save(studentEntity);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                studentEntity.getUsername(),
                registerDTO.password()
        );
        return jwtTokenService.generateJWTToken(authentication);
    }
}
