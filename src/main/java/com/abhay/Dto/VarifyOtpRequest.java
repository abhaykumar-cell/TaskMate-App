package com.abhay.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VarifyOtpRequest {
    @NotNull(message = "Please Input Otp")

    Integer Otp;
    @NotNull(message = "Please Input valid Email")
    @Email
    private String email;
}
