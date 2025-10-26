package com.pos.grad_project.config;

import com.pos.grad_project.model.entity.StudentEntity;
import com.pos.grad_project.repository.StudentRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CustomStudentDetailsService implements UserDetailsService {
    private StudentRepo studentRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        StudentEntity student= studentRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not correct"));
        return new CustomStudentDetails(student);
    }
}
