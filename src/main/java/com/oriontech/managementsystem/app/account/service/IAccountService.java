package com.oriontech.managementsystem.app.account.service;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.oriontech.managementsystem.app.account.dtos.AccountResponse;
import com.oriontech.managementsystem.app.account.enitities.Account;
import com.oriontech.managementsystem.app.account.enums.EAccountStatus;
import com.oriontech.managementsystem.core.utils.AppResponse;

@Repository
public interface IAccountService {

    //For other service
    AccountResponse getAccountByEmail(String email);

    boolean checkExistAccountByEmail(String email);

    AccountResponse saveAccount(Account account);

    AccountResponse updateAccount(Account account);

    AccountResponse updateAccountByEmail(String email);

    boolean deleteAccount(Account account);

    // For Account Controller
    ResponseEntity<AppResponse> getAccounts();

    ResponseEntity<AppResponse> getAccountsByStatus(EAccountStatus status);

}
