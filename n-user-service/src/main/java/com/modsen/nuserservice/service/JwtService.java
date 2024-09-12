package com.modsen.nuserservice.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.modsen.nuserservice.entity.User;

import com.modsen.nuserservice.model.TokenResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JwtService {

    @Value("${jwt.key}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Long validityInMilliSeconds;

    @PostConstruct
    protected void init() {
        jwtSecret = Base64.getEncoder().encodeToString(jwtSecret.getBytes());
    }

    public TokenResponse generateToken(User user) {
        if(!user.isEnabled())
            return new TokenResponse(null);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliSeconds);

        String token = JWT.create()
                .withSubject(user.getEmail())
                .withClaim("id", user.getId())
                .withClaim("authorities", user.getRole().getAuthorities().stream().map(SimpleGrantedAuthority::getAuthority).toList())
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .sign(Algorithm.HMAC256(jwtSecret));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new TokenResponse(token);
    }
    public Map<String, Object> extractClaims(String jwt){
        Map<String, Object> claims = new HashMap<>();
        DecodedJWT decodedJWT = decodeJWT(jwt);
        claims.put("id", decodedJWT.getClaim("id").asLong());
        claims.put("username", decodedJWT.getSubject());
        claims.put("authorities", decodedJWT.getClaim("authorities").asList(String.class));
        return claims;
    }

    public String extractUsername(String token) {
        DecodedJWT decodedJWT = decodeJWT(token);
        return decodedJWT.getSubject();
    }
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private DecodedJWT decodeJWT(String token) {
        JWTVerifier verifier = JWT
                .require(Algorithm.HMAC256(jwtSecret))
                .build();
        return Optional.ofNullable(verifier.verify(token))
                .orElseThrow(() -> new RuntimeException("Invalid token " + token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return decodeJWT(token).getExpiresAt();
    }
}
