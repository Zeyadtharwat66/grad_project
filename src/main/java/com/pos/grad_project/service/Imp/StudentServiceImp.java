package com.pos.grad_project.service.Imp;
import com.pos.grad_project.model.dto.LoginDTO;
import com.pos.grad_project.model.dto.RegisterDTO;
import com.pos.grad_project.model.entity.StudentEntity;
import com.pos.grad_project.repository.StudentRepo;
import com.pos.grad_project.service.JWTTokenService;
import com.pos.grad_project.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@AllArgsConstructor
public class StudentServiceImp implements StudentService {
    private final JWTTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;
    private final StudentRepo studentRepo;
    private final PasswordEncoder passwordEncoder;
    @Override
    public ResponseEntity<?> login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password())
        );
        return ResponseEntity.ok(Map.of("token",jwtTokenService.generateJWTToken(authentication)));
    }
    @Override
    public ResponseEntity<?> register(RegisterDTO registerDTO) {
        if(studentRepo.findByUsername(registerDTO.username()).isPresent()){
            throw new RuntimeException("Username already exists!");
        }
        StudentEntity studentEntity = StudentEntity.builder()
                .username(registerDTO.username())
                .password(passwordEncoder.encode(registerDTO.password()))
                .role(registerDTO.role() != null ? registerDTO.role() : "STUDENT")
                .birthDate(registerDTO.birthDate())
                .email(registerDTO.email())
                .grade(registerDTO.grade())
                .gender(registerDTO.gender())
                .phoneNumber(registerDTO.phoneNumber())
                .build();
        studentRepo.save(studentEntity);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registerDTO.username(), registerDTO.password())
        );
        return ResponseEntity.ok(Map.of("token",jwtTokenService.generateJWTToken(authentication)));
    }

}
