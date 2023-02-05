package com.oriontech.managementsystem.core.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.oriontech.managementsystem.core.jwt.JwtAuthFilter;

import lombok.RequiredArgsConstructor;

/*
 * The prePostEnabled property enables Spring Security pre/post annotations.:  SpEL destekler
 *  @PreAuthorize("hasRole('ROLE_VIEWER')") | @PreAuthorize("hasRole('ROLE_VIEWER') or hasRole('ROLE_EDITOR')")
 *  @PreAuthorize("#username == authentication.principal.username") 
 *  ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
 *  @PrePostAuthorize : Yöntem sonucuna erişme olanığı sunar
 *  @PostAuthorize("returnObject.username == authentication.principal.nickName")
 *  public CustomUser loadUserDetail(String username) {
 *      return userRoleRepository.loadUserByUserName(username);
 *  }
 * ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
 * ------------------------------------------------------------------------------------------------------
 * The securedEnabled property determines if the @Secured annotation should be enabled. : SpEL desteklemez
 * @Secured("ROLE_VIEWER") 
 * ------------------------------------------------------------------------------------------------------
 * The jsr250Enabled property allows us to use the @RoleAllowed annotation.: SpEL desteklemez
 * @RolesAllowed("ROLE_VIEWER") 
 * ------------------------------------------------------------------------------------------------------
 * jsd250 = secured
 */

@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
@EnableMethodSecurity(prePostEnabled  =  true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final UnauthorizedEntryPoint unauthorizedEntryPoint;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeHttpRequests()
                //.requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/error").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint).and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        

        return httpSecurity.build();
    }

}
