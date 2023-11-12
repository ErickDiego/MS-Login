package com.example.demo.controller;

import com.example.demo.JWT.JwtGenerator;
import com.example.demo.entity.ResponseErrorEntity;
import com.example.demo.entity.RequestLoginEntity;
import com.example.demo.entity.ResponseSignUpEntity;
import com.example.demo.entity.UsuarioEntity;
import com.example.demo.repository.UsuarioRepository;
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
                return new ResponseEntity<>(error, HttpStatus.BAD_GATEWAY) ;
            }
            usuarioEntity.setDateCreation(fechaActual);
            usuarioEntity.setLastLogin(fechaActual);
            usuarioEntity.setActive(true);
            //return new ResponseEntity<>(usuarioEntity, HttpStatus.OK) ;

            return new ResponseEntity<>(respuestaCorrecta(usuarioRepository.save(usuarioEntity)), HttpStatus.OK) ;
        } catch (Exception ex) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            ResponseErrorEntity error = new ResponseErrorEntity(timestamp.toString(), 500, "Ha ocurrido un problema, inténtelo más tarde");
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
    private ResponseSignUpEntity respuestaCorrecta(UsuarioEntity usuario){
            ResponseSignUpEntity responseUsuarioEntity = new ResponseSignUpEntity() ;
            responseUsuarioEntity.setId(usuario.getId());
            responseUsuarioEntity.setCreated(usuario.getDateCreation());
            responseUsuarioEntity.setLastLogin(usuario.getLastLogin());
            responseUsuarioEntity.setToken(JwtGenerator.generateToken(usuario));
            responseUsuarioEntity.setAcive(usuario.isActive());

            return  responseUsuarioEntity;
    }

    private boolean validarPassword(UsuarioEntity usuario){
        try {

            //if (usuario.getPassword()== usuarioRepository.findBy())

            return true;
        }catch (Exception ex){

            return  false;
        }
    }

    @RequestMapping("login")
    public String login(@RequestBody RequestLoginEntity token){

      String idUSer = JwtGenerator.verifyToken(token.getToken());


      return  idUSer;
       /** Optional<UsuarioEntity> usuario = Optional.of(new UsuarioEntity());
        String iduser = "";
        usuario = usuarioRepository.findById(iduser);
        **/
    }

}
