package com.abhay.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterUser {
    @NotNull(message = "Name cannot be null")
    private String name;
    @Email
    @NotNull(message = "Email connot be null")
    private String email;
    @NotNull(message = "password Can not be null")
    private String password;
    @NotNull(message = "Role is required")
    private String role;
}
