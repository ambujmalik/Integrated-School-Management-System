package com.ismp.util;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * JWT Utility class for token generation and validation.
 * Handles JWT token creation, validation, and claims extraction.
 */
@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    private static final long TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 Hours
    private static final String ROLE_CLAIM = "role";

    @Value("${jwt.secret:YourSecretKeyForJWTSigningMustBeVeryLongAndSecure!!}")
    private String secret;

    private Key key;

    /**
     * Initialize the signing key from the secret.
     */
    public void init() {
        if (this.key == null) {
            this.key = Keys.hmacShaKeyFor(secret.getBytes());
            logger.debug("JWT signing key initialized");
        }
    }

    /**
     * Generates a JWT token for the given username and role.
     *
     * @param username the username
     * @param role the user role
     * @return JWT token
     * @throws IllegalArgumentException if username or role is null
     */
    public String generateToken(String username, String role) {
        init();

        if (username == null || username.trim().isEmpty()) {
            logger.warn("Token generation attempted with null/empty username");
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        if (role == null || role.trim().isEmpty()) {
            logger.warn("Token generation attempted with null/empty role");
            throw new IllegalArgumentException("Role cannot be null or empty");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put(ROLE_CLAIM, role);

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        logger.debug("JWT token generated for user: {}", username);
        return token;
    }

    /**
     * Validates if a token is valid for the given username.
     *
     * @param token the JWT token
     * @param username the expected username
     * @return true if token is valid, false otherwise
     */
    public boolean validateToken(String token, String username) {
        init();

        try {
            final String extractedUsername = extractUsername(token);
            boolean isValid = extractedUsername.equals(username) && !isTokenExpired(token);

            if (isValid) {
                logger.debug("Token validated successfully for user: {}", username);
            } else {
                logger.warn("Token validation failed for user: {}", username);
            }

            return isValid;

        } catch (ExpiredJwtException e) {
            logger.warn("Token has expired for user: {}", username);
            return false;
        } catch (JwtException e) {
            logger.warn("Token validation failed: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid token: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Extracts the username from a JWT token.
     *
     * @param token the JWT token
     * @return the username
     * @throws JwtException if token is invalid
     */
    public String extractUsername(String token) {
        init();
        return extractAllClaims(token).getSubject();
    }

    /**
     * Extracts the role from a JWT token.
     *
     * @param token the JWT token
     * @return the user role
     * @throws JwtException if token is invalid
     */
    public String extractRole(String token) {
        init();
        return (String) extractAllClaims(token).get(ROLE_CLAIM);
    }

    /**
     * Extracts all claims from a JWT token.
     *
     * @param token the JWT token
     * @return the claims
     * @throws JwtException if token is invalid
     */
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            logger.warn("Token parsing failed: token expired");
            throw e;
        } catch (JwtException e) {
            logger.warn("Token parsing failed: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Checks if a JWT token has expired.
     *
     * @param token the JWT token
     * @return true if token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        try {
            return extractAllClaims(token).getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            logger.debug("Token is expired");
            return true;
        }
    }
}
