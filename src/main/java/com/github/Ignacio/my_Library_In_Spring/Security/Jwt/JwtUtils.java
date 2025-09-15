package com.github.Ignacio.my_Library_In_Spring.Security.Jwt;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    private final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.app.password}")
    private String password;

    @Value("${spring.app.timeDuring}")
    private int timeDuring;

    public String getJwtFromHeader(HttpServletRequest request){
        String headerRequest = request.getHeader("Authorization");

        if(headerRequest != null && headerRequest.startsWith("Bearer ")){
            return headerRequest.substring(7);
        }
        return null;
    }

    public String generateTokenByName(UserDetails userDetails){
        Date now = new Date();
        String token = Jwts.builder()
                .subject(userDetails.getUsername())
                .expiration(new Date(now.getTime() + timeDuring))
                .signWith(key())
                .issuedAt(now)
                .compact();
        logger.info("TOKEN GENERADO: {}", token);
        return token;
    }

    public String getUsernameFromToken(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isValid(String token){
        try{
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public Key key(){
        logger.debug("Key Base64: {}", password);
        logger.debug("Longitud Base64: {}", password.length());
        byte[] keyBytes = Decoders.BASE64.decode(password);
        return Keys.hmacShaKeyFor(keyBytes);

    }
}
