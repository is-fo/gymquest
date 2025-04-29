package org.example.util

import io.javalin.http.Context
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
                .setSigningKey(SECRET)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject()
    }

    static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET)
                    .build()
                    .parseClaimsJws(token)

            return true
        } catch (Exception ignored) {
            return false
        }
    }

    // https://jwt.io/introduction
    static String extractBearerToken(Context it) {
        def authHeader = it.header("Authorization")

        // authHeader kan vara null (jag har testat) och då får man 401 tillbaka i Postman
        if (!authHeader?.startsWith("Bearer ")) {
            it.status(401).result("Missing or malformed Auth header")
            return null
        }

        return authHeader.replace("Bearer ", "")
    }
}
