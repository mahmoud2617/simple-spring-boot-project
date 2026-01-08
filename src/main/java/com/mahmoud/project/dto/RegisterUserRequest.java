package com.mahmoud.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RegisterUserRequest {
    String name;
    String email;
    String password;
}
