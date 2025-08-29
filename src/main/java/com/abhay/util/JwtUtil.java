package com.abhay.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret ;

    private SecretKey getSecretKey(){
        SecretKey skey = Keys.hmacShaKeyFor(secret.getBytes());
        return skey;
    }

    public String generateToken(String username, List<String> roles){
        return Jwts.builder()
                .setSubject(username)
                .claim("roles",roles)
                .setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis()+1000*60*60))//1 Minutes
                .signWith(getSecretKey()).compact();
    }
    public Claims extractClaimsJws(String token){
        JwtParser jParser = Jwts.parserBuilder().setSigningKey(getSecretKey()).build();
        Jws<Claims> jwsClaims = jParser.parseClaimsJws(token);
        return jwsClaims.getBody();
    }

    public String extractEmail(String token){
        return  extractClaimsJws(token).getSubject();
    }
    public List<String> extractRoles(String token){
        return (List<String>) extractClaimsJws(token).get("roles");
    }

    public boolean isTokenExpired(String token){
        return extractClaimsJws(token).getExpiration().before(new Date());
    }
    public boolean validateToken(String token,String email){
        return (extractEmail(token).equals(email) && !isTokenExpired(token));
    }

}
