package com.oriontech.managementsystem.app.auth.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.oriontech.managementsystem.app.account.dtos.AccountRequest;
import com.oriontech.managementsystem.app.account.enums.EAccountRole;
import com.oriontech.managementsystem.app.account.enums.EAccountStatus;
import com.oriontech.managementsystem.app.account.service.AccountService;
import com.oriontech.managementsystem.app.auth.dtos.AuthenticationResponse;
import com.oriontech.managementsystem.app.auth.dtos.SignInRequest;
import com.oriontech.managementsystem.app.auth.dtos.SignUpRequest;
import com.oriontech.managementsystem.core.constants.ErrorMessages;
import com.oriontech.managementsystem.core.jwt.CustomUsersDetailService;
import com.oriontech.managementsystem.core.jwt.JwtService;
import com.oriontech.managementsystem.core.utils.AppResponse;
import com.oriontech.managementsystem.core.utils.EProcessStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {

    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUsersDetailService customUsersDetailService;

    @Override
    public ResponseEntity<AppResponse> signup(SignUpRequest request) {
        log.info("signup -> : /auth/signup");
        try {
            if (!accountService.checkExistAccountByEmail(request.getEmail())) {
                var user = accountService.saveAccountforSignUp(AccountRequest.builder()
                        .fullname(request.getFullname())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .phoneNumber(request.getPhoneNumber())
                        .role(EAccountRole.ROLE_USER)
                        .status(EAccountStatus.VERIFICATION)
                        .build());
                // var jwtToken = jwtService.generateToken(user.getEmail(), user.getRole());
                log.info("signup-> account : {}", user);
                log.info("signup->  Account created");
                return new ResponseEntity<AppResponse>(AppResponse.builder()
                        .status(EProcessStatus.SUCCESS)
                        .message("Registration successfully")
                        .response(user)
                        .build(), HttpStatus.CREATED);

            } else {
                log.info("signup-> User already existed");
                return new ResponseEntity<AppResponse>(AppResponse.builder()
                        .status(EProcessStatus.FAIL)
                        .message("Registration unsuccessfully")
                        .response(ErrorMessages.EMAIL_ALREADY_EXIST)
                        .build(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.info("signup-> Exception : {}", e.toString());
            return new ResponseEntity<AppResponse>(AppResponse.builder()
                    .status(EProcessStatus.FAIL)
                    .message("Registration unsuccessfully")
                    .response(e.getMessage())
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<AppResponse> signin(SignInRequest request) {
        log.info("signin -> : /auth/signin");
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            log.info("signin->  User is authenticated");
            log.info("signin-> User Status : {}", customUsersDetailService.getUserDetail().toString());
            log.info("signin-> check UserStatus : User is active user");
            if (customUsersDetailService.getUserDetail().getStatus().name().equals(EAccountStatus.ACTIVE.name())) {
                log.info("signin-> check UserStatus : active");
                log.info("signin-> Auth user and generate token");
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwtToken = jwtService.generateToken(authentication);
                return new ResponseEntity<AppResponse>(AppResponse.builder()
                        .status(EProcessStatus.SUCCESS)
                        .message("Login successfully")
                        .response(AuthenticationResponse.builder().token(jwtToken).build())
                        .build(), HttpStatus.OK);
            } else {
                log.info("signin-> check UserStatus : not active");
                return new ResponseEntity<AppResponse>(AppResponse.builder()
                        .status(EProcessStatus.FAIL)
                        .message("Login unsuccessfully")
                        .response("Wait for admin approval")
                        .build(), HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            log.info("signin-> Exception: {}", e.getLocalizedMessage());
            return new ResponseEntity<AppResponse>(
                    AppResponse.builder()
                            .status(EProcessStatus.FAIL)
                            .message("Login unsuccsessfully")
                            .response(e.getMessage())
                            .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }



}
