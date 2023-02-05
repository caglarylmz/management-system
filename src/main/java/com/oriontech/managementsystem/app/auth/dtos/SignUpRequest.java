package com.oriontech.managementsystem.app.auth.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "Invalid Name: Name is not empty")
    @Size(min = 3, max = 30, message = "Invalid Name: Must be of 3 - 30 characters")
    private String fullname;
    @NotBlank(message = "Invalid Phone: Phone is not empty")
    @Pattern(regexp = "^\\d{10}$", message = "Invalid Phone Number : Phone Number is numeric and 10 chracter")
    private String phoneNumber;
    @NotBlank(message = "Invalid Email: Email is not empty")
    @Email(message = "Invalid Email: Invalid email address")
    private String email;
    @NotBlank(message = "Invalid Password: Password is not empty")
    @Size(min = 6, max = 30, message = "Invalid Password: Must be of 6 - 30 characters")
    private String password;
    
}
