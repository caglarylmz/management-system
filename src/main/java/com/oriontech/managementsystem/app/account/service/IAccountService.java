package com.oriontech.managementsystem.app.account.service;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.oriontech.managementsystem.app.account.dtos.AccountRequest;
import com.oriontech.managementsystem.app.account.dtos.AccountResponse;
import com.oriontech.managementsystem.app.account.enums.EAccountStatus;
import com.oriontech.managementsystem.core.utils.AppResponse;

@Repository
public interface IAccountService {

    boolean checkExistAccountByEmail(String email);

    AccountResponse getAccountByEmail(String email);

    AccountResponse saveAccountforSignUp(AccountRequest accountRequest);

    ResponseEntity<AppResponse> saveAccount(AccountRequest accountRequest);

    ResponseEntity<AppResponse> updateAccount(AccountRequest accountRequest);

    ResponseEntity<AppResponse> deleteAccount(AccountRequest accountRequest);

    ResponseEntity<AppResponse> getAccounts();

    ResponseEntity<AppResponse> getAccountsByStatus(EAccountStatus status);

}
