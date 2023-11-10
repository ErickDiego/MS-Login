package com.example.demo.controller;

import com.example.demo.entity.ErrorEntity;
import com.example.demo.entity.ResponseUsuarioEntity;
import com.example.demo.entity.UsuarioEntity;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {
    @Autowired
    private UsuarioRepository usuarioRepository;
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
                ErrorEntity error = new ErrorEntity(timestamp.toString(), 400, "Formato de contraseña invalida");
                return new ResponseEntity<>(error, HttpStatus.BAD_GATEWAY) ;
            }
            usuarioEntity.setDateCreation(fechaActual);
            usuarioEntity.setLastLogin(fechaActual);
            usuarioEntity.setActive(true);
            //return new ResponseEntity<>(usuarioEntity, HttpStatus.OK) ;

            return new ResponseEntity<>(respuestaCorrecta(usuarioRepository.save(usuarioEntity)), HttpStatus.OK) ;
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
        return password.matches("^(?=.*[A-Z])(?=.*\\d.*\\d)[a-zA-Z\\d]{8,12}$");
    }

    private String encriptacionPassword(String passwordInput) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(passwordInput.getBytes(StandardCharsets.UTF_8));
        String passwordOutput = Base64.getEncoder().encodeToString(hash);
        return passwordOutput;
    }

    /**Se revcibe el registro que esta en la BD*/
    private ResponseUsuarioEntity respuestaCorrecta(UsuarioEntity usuario){
            ResponseUsuarioEntity responseUsuarioEntity = new ResponseUsuarioEntity() ;
            responseUsuarioEntity.setId(usuario.getId());
            responseUsuarioEntity.setCreated(usuario.getDateCreation());
            responseUsuarioEntity.setLastLogin(usuario.getLastLogin());
            responseUsuarioEntity.setToken("");
            responseUsuarioEntity.setAcive(usuario.isActive());


            return  responseUsuarioEntity;

    }

}
