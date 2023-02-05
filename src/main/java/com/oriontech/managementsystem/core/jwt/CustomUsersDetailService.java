package com.oriontech.managementsystem.core.jwt;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.oriontech.managementsystem.app.account.enitities.Account;
import com.oriontech.managementsystem.app.account.repository.AccountRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUsersDetailService implements UserDetailsService{
    private final AccountRepository accountRepository;
    private Account account;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername", username);
        account = accountRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		grantedAuthorities.add(new SimpleGrantedAuthority(account.getRole().name()));
        log.info("Success loadUser", account);
        return new User(account.getEmail(),account.getPassword(), grantedAuthorities);
    }

    public Account getUserDetail(){
        account.setPassword(null);
        return account;
    }
    
}
