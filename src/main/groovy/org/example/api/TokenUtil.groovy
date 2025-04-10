package org.example.api

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys

class TokenUtil {
    static final SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    static String generateToken(String username) {
        return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 3600000))
        .signWith(SECRET)
        .compact()
    }

    static String getUsername(String token) {
        return Jwts.parserBuilder()
        .setSigningKey(token)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject()
    }
}
