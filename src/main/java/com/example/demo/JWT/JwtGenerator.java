package com.example.demo.JWT;

import com.example.demo.entity.UsuarioEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtGenerator {
    // Clave secreta para firmar y verificar el token (debe ser mantenida en secreto)
    private static final String CLAVE_SECRETA = "DT7KDvWQwdssnJFMdlgrQipmwgEcuU6snYRjzefd9FQ=";

    public static String generateToken(String idUsuario) {
        // Clave secreta para firmar el token (asegúrate de que sea lo suficientemente segura)
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        System.out.println("Key: "+ key );
        // Fecha de emisión y fecha de vencimiento del token
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 3600000); // Caduca en 1 hora

        // Crear un mapa con los campos del "subject"
        Map<String, Object> subjectClaims = new HashMap<>();
        subjectClaims.put("userId", idUsuario);

        // Generar el token
        String jwt  = Jwts.builder()
                .setClaims(subjectClaims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, CLAVE_SECRETA)
                .compact();

        return jwt;
    }

    public static String verifyToken(String token) {
        //System.out.println("Token recibido: "+ token);
        try {
            // Decodificar y verificar el token
            Claims claims =  Jwts.parser()
                    .setSigningKey(CLAVE_SECRETA)
                    .parseClaimsJws(token)
                    .getBody();

            String userId = claims.get("userId", String.class);
            System.out.println(userId);
            return userId;
        } catch (Exception e) {
            // El token no pudo ser verificado
            System.out.println("Error al verificar el token: " + e.getMessage());
           // return false;
            return  "";
        }
    }
}
