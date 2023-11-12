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

    public static String generateToken(UsuarioEntity usuario) {
        // Clave secreta para firmar el token (asegúrate de que sea lo suficientemente segura)
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        System.out.println("Key: "+ key );
        // Fecha de emisión y fecha de vencimiento del token
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 3600000); // Caduca en 1 hora

        // Crear un mapa con los campos del "subject"
        Map<String, Object> subjectClaims = new HashMap<>();
        subjectClaims.put("userId", usuario.getId());
        subjectClaims.put("username", usuario.getName());
        subjectClaims.put("lastLogin", usuario.getLastLogin());

        // Generar el token
   /**     String jwt = Jwts.builder()
                .setSubject(subjectClaims.toString()) // El "subject" generalmente contiene información del usuario
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key)
                .compact(); **/
        Key claveSecreta = generarClaveSecretaHS256();
        System.out.println("Clave secreta generada: " + Base64.getEncoder().encodeToString(claveSecreta.getEncoded()));

        String jwt  = Jwts.builder()
                .setClaims(subjectClaims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, CLAVE_SECRETA)
                .compact();
        System.out.println("Token generado 1: " + jwt);
        return jwt;
    }

    public static String verifyToken(String token) {
        // Clave secreta para verificar la firma del token (debe coincidir con la clave utilizada para la generación)
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        System.out.println("Token recibido: "+ token);
        try {
            // Decodificar y verificar el token
            Claims claims =  Jwts.parser()
                    .setSigningKey(CLAVE_SECRETA)
                    .parseClaimsJws(token)
                    .getBody();

            // Puedes acceder a otros claims personalizados aquí si los hubieras incluido al generar el token
            // Ejemplo: String userId = claims.get("userId", String.class);
            String userId = claims.get("userId", String.class);
            System.out.println(userId);
            return userId;
            //return true;
        } catch (Exception e) {
            // El token no pudo ser verificado
            System.out.println("Error al verificar el token: " + e.getMessage());
           // return false;
            return  "";
        }
    }

    public static Key generarClaveSecretaHS256() {
        // Utilizar Keys.secretKeyFor para generar una clave segura para HS256
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

}
