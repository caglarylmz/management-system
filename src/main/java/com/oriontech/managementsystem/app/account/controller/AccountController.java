package com.oriontech.managementsystem.app.account.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oriontech.managementsystem.app.account.enums.EAccountStatus;
import com.oriontech.managementsystem.app.account.service.AccountService;
import com.oriontech.managementsystem.core.utils.AppResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<AppResponse> getAllAccounts() {
        return accountService.getAccounts();
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AppResponse> getActiveAccounts() {
        return accountService.getAccountsByStatus(EAccountStatus.ACTIVE);
    }

    @GetMapping("/verification")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AppResponse> getVerificationAccounts() {
        return accountService.getAccountsByStatus(EAccountStatus.VERIFICATION);
    }

    @GetMapping("/deactive")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AppResponse> getDeactiveAccounts() {
        return accountService.getAccountsByStatus(EAccountStatus.DEACTIVE);
    }
}
