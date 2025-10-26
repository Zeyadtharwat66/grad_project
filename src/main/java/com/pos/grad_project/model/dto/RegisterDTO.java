package com.pos.grad_project.model.dto;

import org.springframework.boot.context.properties.bind.DefaultValue;

public record RegisterDTO(String username, String password,@DefaultValue("STUDENT") String role) {}
