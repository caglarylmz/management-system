package com.oriontech.managementsystem.core.jwt;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtService {
    @Value("${jwt.token.validity}")
    public long TOKEN_VALIDITY;

    @Value("${jwt.signing.key}")
    public String SIGNING_KEY;

    @Value("${jwt.authorities.key}")
    public String AUTHORITIES_KEY;

    public String generateToken(Authentication authentication) {
        log.info("JWTService :: generateToken");
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        log.info("JWTService :: validateToken");
        final String username = getUsernameFromToken(token);
        log.info("JWTService :: validateToken -> Token Username : " + username);
        log.info("JWTService :: validateToken -> SignedIn Username : " + userDetails.getUsername());
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    UsernamePasswordAuthenticationToken getAuthenticationToken(final String token,
            final Authentication existingAuth,
            final UserDetails userDetails) throws Exception {

        log.info("JWTService :: getAuthenticationToken->");

        try {
            log.info("JWTService :: getAuthenticationToken-> JwtParser");

            final JwtParser jwtParser = Jwts.parser().setSigningKey(SIGNING_KEY);
            final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
            final Claims claims = claimsJws.getBody();

            log.info("JWTService :: getAuthenticationToken-> claims : {}", claims.toString());

            final Collection<? extends GrantedAuthority> authorities = Arrays
                    .stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
        } catch (SignatureException e) {
            log.error("JWTService :: getAuthenticationToken-> SignatureException : {}", e.getLocalizedMessage());
            throw new Exception(e.getLocalizedMessage());
        } catch (MalformedJwtException e) {
            log.error("JWTService :: getAuthenticationToken-> Invalid JWT token: {}", e.getMessage());
            throw new Exception(e.getLocalizedMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWTService :: getAuthenticationToken-> JWT token is expired: {}", e.getMessage());
            throw new Exception(e.getLocalizedMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWTService :: getAuthenticationToken-> JWT token is unsupported: {}", e.getMessage());
            throw new Exception(e.getLocalizedMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWTService :: getAuthenticationToken-> JWT claims string is empty: {}", e.getMessage());
            throw new Exception(e.getLocalizedMessage());
        }

    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

}
