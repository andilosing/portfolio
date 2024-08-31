package com.dynamics.users.security;

import com.dynamics.users.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    //tbd check ob das so richtig, einmal access token nimmt user andere refresh eben nicht

    private final String JWT_SECRET = Base64.getEncoder().encodeToString("aslkdfjehghöfdgoiegferjgrelgaj".getBytes());
    private final long JWT_EXPIRATION_REFRESH = 30L * 24 * 60 * 60 * 1000; // 30 days
    private final long JWT_EXPIRATION_ACCESS = 60 * 60 * 1000; // 1 hour

    public String generateAccessToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION_ACCESS);

        List<String> roleIds = user.getRoles().stream()
                .map(role -> role.getRoleId().toString())
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getUserId().toString())
                .claim("roles", roleIds)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public String generateRefreshToken(String username, UUID userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION_REFRESH);

        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public Date getAccessTokenExpiryDate() {
        return new Date(System.currentTimeMillis() + JWT_EXPIRATION_ACCESS);
    }

    public Date getRefreshTokenExpiryDate() {
        return new Date(System.currentTimeMillis() + JWT_EXPIRATION_REFRESH);
    }

    public UUID getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();

        return UUID.fromString(claims.get("userId", String.class));
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Invalid Token");
        }
    }

}
