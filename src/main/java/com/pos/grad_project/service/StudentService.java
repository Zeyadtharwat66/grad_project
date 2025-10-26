package com.pos.grad_project.service;

import com.pos.grad_project.model.dto.LoginDTO;
import com.pos.grad_project.model.dto.RegisterDTO;

public interface StudentService {
    public String login(LoginDTO loginDTO);
    public String register(RegisterDTO registerDTO);
}
