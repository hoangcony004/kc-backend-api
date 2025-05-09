package kho_cang.api.config.auth;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.Claims;

@Service
public class JwtService {
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${jwt.secret}")
    private String SECRET_KEY;
    private final long EXPIRATION_TIME = 3 * 24 * 60 * 60 * 1000;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username, List<String> roles) {
        String token = Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
        return token;
    }

    public String generateToken(String username) {
        return generateToken(username, Collections.emptyList());
    }

    public String extractUsername(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            String username = claims.getSubject();
            return username;
        } catch (SignatureException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    public List<String> extractRoles(String token) {
        try {
            
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            List<String> roles = claims.get("roles", List.class);
            return roles;
        } catch (SignatureException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean isValidToken(String token) {
        
        if (tokenBlacklistService.isTokenBlacklisted(token)) {
            return false;
        }

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            logger.debug("Token claims: {}", claims);
            return true;
        } catch (SignatureException e) {
            return false;
        } catch (ExpiredJwtException e) {
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public void blacklistToken(String token, String username) {
        tokenBlacklistService.blacklistToken(token, username);
    }
}
