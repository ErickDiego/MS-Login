package com.example.demo.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtGenerator {

    public static String generateToken(String subject) {
        // Clave secreta para firmar el token (asegúrate de que sea lo suficientemente segura)
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        // Fecha de emisión y fecha de vencimiento del token
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 3600000); // Caduca en 1 hora

        // Generar el token
        String jwt = Jwts.builder()
                .setSubject(subject) // El "subject" generalmente contiene información del usuario
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key)
                .compact();

        return jwt;
    }

    public static boolean verifyToken(String token) {
        // Clave secreta para verificar la firma del token (debe coincidir con la clave utilizada para la generación)
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        try {
            // Decodificar y verificar el token
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // En este punto, el token se ha verificado correctamente
            System.out.println("Token verificado. Subject: " + claims.getSubject());
            System.out.println("Fecha de emisión: " + claims.getIssuedAt());
            System.out.println("Fecha de vencimiento: " + claims.getExpiration());

            // Puedes acceder a otros claims personalizados aquí si los hubieras incluido al generar el token
            // Ejemplo: String userId = claims.get("userId", String.class);

            return true;
        } catch (Exception e) {
            // El token no pudo ser verificado
            System.out.println("Error al verificar el token: " + e.getMessage());
            return false;
        }
    }

}
