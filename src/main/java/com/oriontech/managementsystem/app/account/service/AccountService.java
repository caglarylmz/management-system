package com.oriontech.managementsystem.app.account.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.oriontech.managementsystem.app.account.dtos.AccountRequest;
import com.oriontech.managementsystem.app.account.dtos.AccountResponse;
import com.oriontech.managementsystem.app.account.enitities.Account;
import com.oriontech.managementsystem.app.account.enums.EAccountStatus;
import com.oriontech.managementsystem.app.account.repository.AccountRepository;
import com.oriontech.managementsystem.core.constants.ErrorMessages;
import com.oriontech.managementsystem.core.exceptions.UserNotFoundException;
import com.oriontech.managementsystem.core.utils.AppResponse;
import com.oriontech.managementsystem.core.utils.EProcessStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {

    private final AccountRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AccountResponse getAccountByEmail(String userEmail) {

        Account account = repository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return AccountResponse.accountResponseFromAccount(account);

    }

    @Override
    public boolean checkExistAccountByEmail(String email) {
        return repository.existsByEmail(email) ? true : false;
    }

    @Override
    public AccountResponse saveAccountforSignUp(AccountRequest accountRequest) {
        accountRequest.setPassword(passwordEncoder.encode(accountRequest.getPassword()));
        var account = AccountRequest.accountRequestToAccount(accountRequest);
        return AccountResponse.accountResponseFromAccount(repository.save(account));
    }

    @Override
    public ResponseEntity<AppResponse> updateAccount(AccountRequest request) {
        try {
            request.setPassword(passwordEncoder.encode(request.getPassword()));
            var account = repository.save(AccountRequest.accountRequestToAccount(request));
            return new ResponseEntity<AppResponse>(AppResponse.builder()
                    .status(EProcessStatus.SUCCESS)
                    .message("Update Account process successfully")
                    .response(account)
                    .build(), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<AppResponse>(AppResponse.builder()
                    .status(EProcessStatus.FAIL)
                    .message("Update Account process unsuccessfully")
                    .response(ErrorMessages.ACCOUNT_NOT_FOUND)
                    .build(), HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public ResponseEntity<AppResponse> deleteAccount(AccountRequest request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<AppResponse> getAccounts() {
        try {

            List<AccountResponse> aResponses = new ArrayList<>();
            List<Account> accounts = repository.findAll();

            accounts.forEach(a -> aResponses.add(AccountResponse.accountResponseFromAccount(a)));
            return new ResponseEntity<AppResponse>(AppResponse.builder()
                    .status(EProcessStatus.SUCCESS)
                    .message("All Accounts")
                    .response(aResponses)
                    .build(), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<AppResponse>(AppResponse.builder()
                    .status(EProcessStatus.FAIL)
                    .message("Error")
                    .response(e.getMessage())
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<AppResponse> getAccountsByStatus(EAccountStatus status) {

        try {
            // if (jwtAuthFilter.isAdmin()) {
            List<AccountResponse> aResponses = new ArrayList<>();
            List<Account> accounts = repository.findByStatus(status);
            accounts.forEach(a -> aResponses.add(AccountResponse.accountResponseFromAccount(a)));

            return new ResponseEntity<AppResponse>(AppResponse.builder()
                    .status(EProcessStatus.SUCCESS)
                    .message("All Accounts By Status")
                    .response(aResponses)
                    .build(), HttpStatus.OK);
            // } else {
            // throw new AccessDeniedException("Access Denied");
            // }
        } catch (Exception e) {
            return new ResponseEntity<AppResponse>(AppResponse.builder()
                    .status(EProcessStatus.FAIL)
                    .message("Error")
                    .response(e.getMessage())
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @Override
    public ResponseEntity<AppResponse> saveAccount(AccountRequest accountRequest) {
        // TODO Auto-generated method stub
        return null;
    }

}

/*
 * @Override
 * public ResponseEntity<BaseResponse> saveUser(UserRequest userRequest) {
 * 
 * log.info("Save User Process{}", userRequest.toString());
 * try {
 * if (!userRepository.existsByEmail(userRequest.getEmail())) {
 * userRepository.save(User.builder()
 * .fullname(userRequest.getFullname())
 * .email(userRequest.getEmail())
 * .password(userRequest.getPassword())
 * .phoneNumber(userRequest.getPhoneNumber())
 * .role(UserRole.USER)
 * .status(UserStatus.VERIFICATION)
 * .build());
 * log.info("Registration process successfully : User created");
 * 
 * return new ResponseEntity<BaseResponse>(BaseResponse.builder()
 * .status(EProcessStatus.SUCCESS)
 * .message("User Created")
 * .object(UserResponse.builder()
 * .fullname(userRequest.getFullname())
 * .email(userRequest.getEmail())
 * .phoneNumber(userRequest.getPhoneNumber())
 * .build())
 * .build(), HttpStatus.CREATED);
 * 
 * } else {
 * log.info("Registration process unsuccessfully : User already registered.");
 * return new ResponseEntity<BaseResponse>(BaseResponse.builder()
 * .status(EProcessStatus.FAIL)
 * .message(ErrorMessages.EMAIL_ALREADY_EXIST)
 * .build(), HttpStatus.OK);
 * }
 * 
 * } catch (Exception e) {
 * // e.printStackTrace();
 * log.info("Registration process unsuccessfully : {}", e.toString());
 * return new ResponseEntity<BaseResponse>(BaseResponse.builder()
 * .status(EProcessStatus.FAIL)
 * .message(ErrorMessages.SOMETHING_WENT_WRONG)
 * .build(), HttpStatus.BAD_REQUEST);
 * }
 * 
 * }
 * 
 */