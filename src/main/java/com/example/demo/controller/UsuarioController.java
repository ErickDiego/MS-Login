package com.example.demo.controller;

import com.example.demo.JWT.JwtGenerator;
import com.example.demo.entity.*;
import com.example.demo.repository.UsuarioRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

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
            String fechaActual = LocalDate.now().toString();//String.valueOf(date.getTime());
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            if (validacionPassword(usuarioEntity.getPassword())) {
                System.out.println("contraseña Valida");
                usuarioEntity.setPassword(encriptacionPassword(usuarioEntity.getPassword()));
            } else {
                ResponseErrorEntity error = new ResponseErrorEntity(timestamp.toString(), 400, "Formato de contraseña invalida");
                return new ResponseEntity<>(error, HttpStatus.BAD_GATEWAY);
            }
            usuarioEntity.setDateCreation(fechaActual);
            usuarioEntity.setLastLogin(fechaActual);
            usuarioEntity.setActive(true);

            return new ResponseEntity<>(respuestaCorrecta(usuarioRepository.save(usuarioEntity)), HttpStatus.OK);
        } catch (Exception ex) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            ResponseErrorEntity error = new ResponseErrorEntity(timestamp.toString(), 500, "Ha ocurrido un problema, inténtelo más tarde");
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping("login")
    public ResponseEntity<?> login(@RequestBody RequestLoginEntity token) {
        String idUSer = JwtGenerator.verifyToken(token.getToken());

        Optional.of(new UsuarioEntity());
        Optional<UsuarioEntity> usuario;

        usuario = usuarioRepository.findById(idUSer);

        ResponseLoginEntity responseLoginEntity = new ResponseLoginEntity();
        responseLoginEntity.setId(usuario.get().getId());
        responseLoginEntity.setCreated(usuario.get().getDateCreation());
        responseLoginEntity.setLastLogin(usuario.get().getLastLogin());
        responseLoginEntity.setToken(JwtGenerator.generateToken(idUSer));
        responseLoginEntity.setActive(usuario.get().isActive());
        responseLoginEntity.setName(usuario.get().getName());
        responseLoginEntity.setEmail(usuario.get().getEmail());
        responseLoginEntity.setPassword(usuario.get().getPassword());
        responseLoginEntity.setPhones(usuario.get().getPhones());

        return new ResponseEntity<>(responseLoginEntity, HttpStatus.OK);
    }
    /**
     * La clave debe seguir una expresión regular para validar que formato sea el correcto.
     * Debe tener solo una Mayúscula y solamente dos números (no necesariamente
     * consecutivos), en combinación de letras minúsculas, largo máximo de 12 y mínimo 8
     */
    public boolean validacionPassword(String password) {
        return password.matches("^(?=.*[A-Z])(?=.*\\d.*\\d)[a-zA-Z\\d]{8,12}$");
    }

    public String encriptacionPassword(String passwordInput) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(passwordInput.getBytes(StandardCharsets.UTF_8));
        String passwordOutput = Base64.getEncoder().encodeToString(hash);
        return passwordOutput;
    }

    /**
     * Se revcibe el registro que esta en la BD
     */
    private ResponseSignUpEntity respuestaCorrecta(UsuarioEntity usuario) {
        ResponseSignUpEntity responseUsuarioEntity = new ResponseSignUpEntity();
        responseUsuarioEntity.setId(usuario.getId());
        responseUsuarioEntity.setCreated(usuario.getDateCreation());
        responseUsuarioEntity.setLastLogin(usuario.getLastLogin());
        responseUsuarioEntity.setToken(JwtGenerator.generateToken(usuario.getId()));
        responseUsuarioEntity.setAcive(usuario.isActive());

        return responseUsuarioEntity;
    }

}
