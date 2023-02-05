package com.oriontech.managementsystem.app.auth.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oriontech.managementsystem.app.auth.dtos.SignInRequest;
import com.oriontech.managementsystem.app.auth.dtos.SignUpRequest;
import com.oriontech.managementsystem.app.auth.service.AuthenticationService;
import com.oriontech.managementsystem.core.utils.AppResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse> register(
            @Valid @RequestBody(required = true) SignUpRequest request) {
        return service.signup(request);

    }

    @PostMapping(value = "/signin", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse> authenticate(
            @Valid @RequestBody(required = true) SignInRequest request) {
        return service.signin(request);
    }


}
