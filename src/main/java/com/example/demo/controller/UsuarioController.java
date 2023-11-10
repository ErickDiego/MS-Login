package com.example.demo.controller;

import com.example.demo.entity.ErrorEntity;
import com.example.demo.entity.UsuarioEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String helloWorld() {
        return "Hola Mundo desde el proyecto Login-MS";
    }

    @PostMapping
    @RequestMapping("sign-up")
    public ResponseEntity<?> createUser(@RequestBody UsuarioEntity usuarioEntity) throws NoSuchAlgorithmException {
        try {
            Date date = new Date();
            String fechaActual = String.valueOf(date.getTime());
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            if (validacionPassword(usuarioEntity.getPassword())) {
                System.out.println("contraseña Valida");
                usuarioEntity.setPassword(encriptacionPassword(usuarioEntity.getPassword()));
            } else {
                // @ResponseStatus(HttpStatus.BAD_GATEWAY);
                ErrorEntity error = new ErrorEntity(timestamp.toString(), 400, "Formato de contraseña invalida");
                //error.setCodigo(400);
                //error.setDetail("Formato de contraseña invalida");
                //error.setTimestamp();
                return new ResponseEntity<>(error, HttpStatus.BAD_GATEWAY) ;
            }
            usuarioEntity.setLastLogin(fechaActual);
            usuarioEntity.setActive(true);
            return new ResponseEntity<>(usuarioEntity, HttpStatus.OK) ;
            //return usuarioEntity;
            // usuarioRepository.save(usuarioEntity);
        } catch (Exception ex) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            ErrorEntity error = new ErrorEntity(timestamp.toString(), 500, "Ha ocurrido un problema, inténtelo más tarde");
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * La clave debe seguir una expresión regular para validar que formato sea el correcto.
     * Debe tener solo una Mayúscula y solamente dos números (no necesariamente
     * consecutivos), en combinación de letras minúsculas, largo máximo de 12 y mínimo 8
     */
    private boolean validacionPassword(String password) {
        if (password.length() <= 12 && password.length() >= 8) {
            int cantMayusculas = 0;
            int catnNumeros = 0;
            for (int i = 0; i < password.length(); i++) {
                if (Character.isUpperCase(password.charAt(i))) {
                    cantMayusculas++;
                }
                if (Character.isDigit(password.charAt(i))) {
                    catnNumeros++;
                }
            }

            if (catnNumeros == 2 && cantMayusculas == 1) {
                return true;
            }
        }
        return false;

    }

    private String encriptacionPassword(String passwordInput) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(passwordInput.getBytes(StandardCharsets.UTF_8));
        String passwordOutput = Base64.getEncoder().encodeToString(hash);
        return passwordOutput;
    }

}
