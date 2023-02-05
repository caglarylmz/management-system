package com.oriontech.managementsystem.app.auth.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.oriontech.managementsystem.app.auth.dtos.SignInRequest;
import com.oriontech.managementsystem.app.auth.dtos.SignUpRequest;
import com.oriontech.managementsystem.core.utils.AppResponse;

@Repository
public interface IAuthenticationService {
    ResponseEntity<AppResponse> signup(SignUpRequest request);

    ResponseEntity<AppResponse> signin(SignInRequest request);

}
