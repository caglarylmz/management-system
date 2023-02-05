package com.oriontech.managementsystem.core.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUsersDetailService customUsersDetailService;

    @Value("${jwt.header.string}")
    public String HEADER_STRING;

    @Value("${jwt.token.prefix}")
    public String TOKEN_PREFIX;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader(HEADER_STRING);
        String username = null;
        String authToken = null;

        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            log.info("JwtAuthFilter::doFilterInternal -> check header ");
            authToken = header.replace(TOKEN_PREFIX, "");
            // authToken = header.substring(7);
            try {
                username = jwtService.getUsernameFromToken(authToken);
                log.info("JwtAuthFilter::doFilterInternal -> get username from token");
            } catch (IllegalArgumentException e) {
                log.error("JwtAuthFilter::doFilterInternal -> An error occurred while fetching Username from Token",
                        e.getLocalizedMessage());
            } catch (ExpiredJwtException e) {
                log.error("JwtAuthFilter::doFilterInternal -> The token has expired", e.getLocalizedMessage());
            } catch (SignatureException e) {
                log.error("JwtAuthFilter::doFilterInternal -> Authentication Failed. Username or Password not valid.",
                        e.getLocalizedMessage());
            }
        } else {
            log.warn("JwtAuthFilter::doFilterInternal -> Couldn't find bearer string, header will be ignored");
        }
        log.info("JwtAuthFilter::doFilterInternal -> check authenticaiton ");
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = customUsersDetailService.loadUserByUsername(username);
            log.info("JwtAuthFilter::doFilterInternal -> validate token ");
            if (jwtService.validateToken(authToken, userDetails)) {
                try {
                    log.info("JwtAuthFilter::doFilterInternal -> get token ");
                    UsernamePasswordAuthenticationToken authentication = jwtService.getAuthenticationToken(authToken,
                            SecurityContextHolder.getContext().getAuthentication(), userDetails);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    log.info("authenticated user " + username + ", setting security context");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (Exception e) {
                    log.error("JwtAuthFilter::doFilterInternal -> get token fail : ", e.getLocalizedMessage());
                    throw new ServletException(e.getLocalizedMessage());
                }

            }
        }

        filterChain.doFilter(request, response);

    }

}
