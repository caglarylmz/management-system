package com.oriontech.managementsystem.app.auth.dtos;


import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class AuthenticationResponse{    
    private String token;
}
