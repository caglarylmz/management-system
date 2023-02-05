package com.oriontech.managementsystem.app.auth.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {
    @NotBlank(message = "Invalid Email: Email is not empty")
    @Email(message = "Invalid Email: Invalid email address")
    private String email;
    @NotBlank(message = "Invalid Password: Password is not empty")
    @Size(min = 6, max = 30, message = "Invalid Password: Must be of 6 - 30 characters")
    private String password;
}
