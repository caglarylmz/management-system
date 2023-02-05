package com.oriontech.managementsystem.app.account.dtos;

import com.oriontech.managementsystem.app.account.enitities.Account;
import com.oriontech.managementsystem.app.account.enums.EAccountRole;
import com.oriontech.managementsystem.app.account.enums.EAccountStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class AccountResponse {

    private String fullname;
    private String phoneNumber;
    private String email;
    private EAccountStatus status;
    private EAccountRole role;

    public static AccountResponse accountResponseFromAccount(Account account) {
        return AccountResponse.builder().fullname(account.getFullname())
                .phoneNumber(account.getPhoneNumber())
                .email(account.getEmail())
                .role(account.getRole())
                .status(account.getStatus())
                .build();
    }
}
